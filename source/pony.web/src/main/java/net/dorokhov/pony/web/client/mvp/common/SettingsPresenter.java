package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import net.dorokhov.pony.web.client.event.RefreshEvent;
import net.dorokhov.pony.web.client.service.BusyIndicator;
import net.dorokhov.pony.web.client.service.LibraryScanner;
import net.dorokhov.pony.web.client.service.rpc.ConfigurationServiceRpcAsync;
import net.dorokhov.pony.web.shared.ConfigurationDto;
import net.dorokhov.pony.web.shared.ConfigurationOptions;
import net.dorokhov.pony.web.shared.StatusDto;
import net.dorokhov.pony.web.shared.exception.ConcurrentScanException;
import net.dorokhov.pony.web.shared.exception.LibraryNotDefinedException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> implements LibraryScanner.Delegate, SettingsUiHandlers {

	public interface MyView extends PopupView, HasUiHandlers<SettingsUiHandlers> {

		public static enum ScanState {
			INACTIVE, SCAN_STARTING, SCANNING
		}

		public static enum ContentState {
			LOADING, LOADED, SAVING, ERROR
		}

		public StatusDto getProgress();

		public void setProgress(StatusDto aProgress);

		public List<ConfigurationDto> getConfiguration();

		public void setConfiguration(List<ConfigurationDto> aConfiguration);

		public ScanState getScanState();

		public void setScanState(ScanState aScanState);

		public ContentState getContentState();

		public void setContentState(ContentState aContentState);
	}

	private static final int REFRESH_INTERVAL = 10000;

	private final Logger log = Logger.getLogger(getClass().getName());

	private final LibraryScanner libraryScanner;

	private final ConfigurationServiceRpcAsync configurationService;

	private Timer refreshTimer;

	private Request currentRequest;

	@Inject
	public SettingsPresenter(EventBus aEventBus, MyView aView, LibraryScanner aLibraryScanner, ConfigurationServiceRpcAsync aSettingsService) {

		super(aEventBus, aView);

		libraryScanner = aLibraryScanner;
		configurationService = aSettingsService;

		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {

		super.onBind();

		libraryScanner.addDelegate(this);
	}

	@Override
	protected void onUnbind() {

		libraryScanner.removeDelegate(this);

		super.onUnbind();
	}

	@Override
	protected void onReveal() {

		super.onReveal();

		if (getView().getContentState() != MyView.ContentState.SAVING) {

			log.fine("updating configuration...");

			if (currentRequest != null) {

				currentRequest.cancel();

				BusyIndicator.finishTask();

				log.fine("active settings request cancelled");
			}

			BusyIndicator.startTask();

			getView().setContentState(MyView.ContentState.LOADING);

			currentRequest = configurationService.getAll(new AsyncCallback<List<ConfigurationDto>>() {

				@Override
				public void onSuccess(List<ConfigurationDto> aResult) {

					BusyIndicator.finishTask();

					currentRequest = null;

					getView().setConfiguration(aResult);
					getView().setContentState(MyView.ContentState.LOADED);

					log.fine("configuration updated successfully");
				}

				@Override
				public void onFailure(Throwable aCaught) {

					BusyIndicator.finishTask();

					getView().setContentState(MyView.ContentState.ERROR);

					log.log(Level.SEVERE, "could not update configuration", aCaught);
				}
			});
		}
	}

	@Override
	public void onScanStarted(LibraryScanner aLibraryScanner) {

		getView().setScanState(MyView.ScanState.SCAN_STARTING);

		if (refreshTimer != null) {
			refreshTimer.cancel();
		}

		refreshTimer = new Timer() {
			@Override
			public void run() {
				requestRefresh();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}

	@Override
	public void onScanProgress(LibraryScanner aLibraryScanner, StatusDto aStatus) {

		getView().setProgress(aStatus);

		getView().setScanState(MyView.ScanState.SCANNING);
	}

	@Override
	public void onScanFailed(LibraryScanner aLibraryScanner, Throwable aCaught) {

		getView().setScanState(MyView.ScanState.INACTIVE);

		refreshTimer.cancel();

		refreshTimer = null;

		if (aCaught instanceof LibraryNotDefinedException) {
			Window.alert("Music folders not defined!");
		} else if (aCaught instanceof ConcurrentScanException) {
			Window.alert("Music folders are already being scanned!");
		} else {
			Window.alert("Could not start scanning!");
		}
	}

	@Override
	public void onScanFinished(LibraryScanner aLibraryScanner) {

		getView().setScanState(MyView.ScanState.INACTIVE);

		refreshTimer.cancel();

		refreshTimer = null;

		requestRefresh();
	}

	@Override
	public void onScanRequested() {
		libraryScanner.scan();
	}

	@Override
	public void onSaveRequested(List<ConfigurationDto> aConfiguration) {

		if (getView().getContentState() == MyView.ContentState.LOADED) {

			if (currentRequest != null) {

				currentRequest.cancel();

				BusyIndicator.finishTask();

				log.fine("active settings request cancelled");
			}

			BusyIndicator.startTask();

			getView().setContentState(MyView.ContentState.SAVING);

			final ConfigurationDto originalLibraryConfig = getLibraryConfig(getView().getConfiguration());

			currentRequest = configurationService.save(aConfiguration, new AsyncCallback<List<ConfigurationDto>>() {

				@Override
				public void onSuccess(List<ConfigurationDto> aResult) {

					BusyIndicator.finishTask();

					getView().setConfiguration(aResult);
					getView().setContentState(MyView.ContentState.LOADED);

					log.fine("configuration saved successfully");

					if (shouldOfferScan(getLibraryConfig(aResult), originalLibraryConfig)) {
						if (Window.confirm("Library configuration has been changed. Do you want to scan new library?")) {
							libraryScanner.scan();
						}
					}
				}

				@Override
				public void onFailure(Throwable aCaught) {

					BusyIndicator.finishTask();

					getView().setContentState(MyView.ContentState.LOADED);

					log.log(Level.SEVERE, "could not save configuration", aCaught);

					Window.alert("Could not save configuration!");
				}
			});
		}
	}

	private void requestRefresh() {
		getEventBus().fireEvent(new RefreshEvent(RefreshEvent.REFRESH_REQUESTED));
	}

	private ConfigurationDto getLibraryConfig(List<ConfigurationDto> aConfig) {

		if (aConfig != null) {
			for (ConfigurationDto nextConfig : aConfig) {
				if (nextConfig.getId().equals(ConfigurationOptions.LIBRARY_FOLDERS)) {
					return nextConfig;
				}
			}
		}

		return null;
	}

	private boolean shouldOfferScan(ConfigurationDto aLibraryConfig, ConfigurationDto aOriginalLibraryConfig) {

		String oldValue = aOriginalLibraryConfig != null ? aOriginalLibraryConfig.getValue() : null;
		if (oldValue == null) {
			oldValue = "";
		}

		String newValue = aLibraryConfig != null ? aLibraryConfig.getValue() : null;
		if (newValue == null) {
			newValue = "";
		}

		if (!newValue.equals(oldValue)) {
			return !libraryScanner.isScanning();
		}

		return false;
	}
}
