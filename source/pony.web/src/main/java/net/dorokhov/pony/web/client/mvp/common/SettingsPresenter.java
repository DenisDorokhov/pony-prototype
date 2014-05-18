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
import net.dorokhov.pony.web.client.LocaleMessages;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.event.RefreshEvent;
import net.dorokhov.pony.web.client.service.BusyIndicator;
import net.dorokhov.pony.web.client.service.LibraryScanner;
import net.dorokhov.pony.web.client.service.rpc.ConfigurationServiceRpcAsync;
import net.dorokhov.pony.web.client.service.rpc.LibraryServiceRpcAsync;
import net.dorokhov.pony.web.shared.ConfigurationDto;
import net.dorokhov.pony.web.shared.ConfigurationOptions;
import net.dorokhov.pony.web.shared.ScanResultDto;
import net.dorokhov.pony.web.shared.ScanStatusDto;
import net.dorokhov.pony.web.shared.exception.ConcurrentScanException;
import net.dorokhov.pony.web.shared.exception.LibraryNotDefinedException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> implements LibraryScanner.Delegate, SettingsUiHandlers {

	public interface MyView extends PopupView, HasUiHandlers<SettingsUiHandlers> {

		public static enum ScannerState {
			INACTIVE, SCAN_STARTING, SCANNING
		}

		public static enum ConfigurationState {
			LOADING, LOADED, SAVING, ERROR
		}

		public ScanResultDto getScanResult();

		public void setScanResult(ScanResultDto aScanResult);

		public ScanStatusDto getProgress();

		public void setProgress(ScanStatusDto aProgress);

		public List<ConfigurationDto> getConfiguration();

		public void setConfiguration(List<ConfigurationDto> aConfiguration);

		public ContentState getScanResultState();

		public void setScanResultState(ContentState aScanResultState);

		public ScannerState getScannerState();

		public void setScannerState(ScannerState aScannerState);

		public ConfigurationState getConfigurationState();

		public void setConfigurationState(ConfigurationState aConfigurationState);
	}

	private static final int REFRESH_INTERVAL = 10000;

	private final Logger log = Logger.getLogger(getClass().getName());

	private final LibraryServiceRpcAsync libraryService;

	private final LibraryScanner libraryScanner;

	private final ConfigurationServiceRpcAsync configurationService;

	private Timer refreshTimer;

	private Request currentScanResultRequest;
	private Request currentConfigRequest;

	@Inject
	public SettingsPresenter(EventBus aEventBus, MyView aView, LibraryServiceRpcAsync aLibraryService, LibraryScanner aLibraryScanner, ConfigurationServiceRpcAsync aSettingsService) {

		super(aEventBus, aView);

		libraryService = aLibraryService;
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

		loadScanResult();

		if (getView().getConfigurationState() != MyView.ConfigurationState.SAVING) {
			loadConfiguration();
		}
	}

	@Override
	public void onScanStarted(LibraryScanner aLibraryScanner) {

		getView().setScannerState(MyView.ScannerState.SCAN_STARTING);

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
	public void onScanProgress(LibraryScanner aLibraryScanner, ScanStatusDto aStatus) {

		getView().setProgress(aStatus);

		getView().setScannerState(MyView.ScannerState.SCANNING);
	}

	@Override
	public void onScanFailed(LibraryScanner aLibraryScanner, Throwable aCaught) {

		getView().setScannerState(MyView.ScannerState.INACTIVE);

		refreshTimer.cancel();

		refreshTimer = null;

		if (aCaught instanceof LibraryNotDefinedException) {
			Window.alert(LocaleMessages.IMPL.alertLibraryNotDefined());
		} else if (aCaught instanceof ConcurrentScanException) {
			Window.alert(LocaleMessages.IMPL.alertLibraryAlreadyScanning());
		} else {
			Window.alert(LocaleMessages.IMPL.alertCouldNotStartScanning());
		}
	}

	@Override
	public void onScanFinished(LibraryScanner aLibraryScanner) {

		getView().setScannerState(MyView.ScannerState.INACTIVE);

		refreshTimer.cancel();

		refreshTimer = null;

		loadScanResult();
		requestRefresh();
	}

	@Override
	public void onScanRequested() {
		libraryScanner.scan();
	}

	@Override
	public void onSaveRequested(List<ConfigurationDto> aConfiguration) {

		if (getView().getConfigurationState() == MyView.ConfigurationState.LOADED) {

			if (currentConfigRequest != null) {

				currentConfigRequest.cancel();

				BusyIndicator.finishTask();

				log.fine("active configuration request cancelled");
			}

			BusyIndicator.startTask();

			getView().setConfigurationState(MyView.ConfigurationState.SAVING);

			final ConfigurationDto originalLibraryConfig = getLibraryConfig(getView().getConfiguration());

			currentConfigRequest = configurationService.save(aConfiguration, new AsyncCallback<List<ConfigurationDto>>() {

				@Override
				public void onSuccess(List<ConfigurationDto> aResult) {

					BusyIndicator.finishTask();

					currentConfigRequest = null;

					getView().setConfiguration(aResult);
					getView().setConfigurationState(MyView.ConfigurationState.LOADED);

					log.fine("configuration saved successfully");

					if (shouldOfferScan(getLibraryConfig(aResult), originalLibraryConfig)) {
						if (Window.confirm(LocaleMessages.IMPL.confirmationScanAfterConfigurationChange())) {
							libraryScanner.scan();
						}
					}
				}

				@Override
				public void onFailure(Throwable aCaught) {

					BusyIndicator.finishTask();

					currentConfigRequest = null;

					getView().setConfigurationState(MyView.ConfigurationState.LOADED);

					log.log(Level.SEVERE, "could not save configuration", aCaught);

					Window.alert(LocaleMessages.IMPL.alertCouldNotSaveConfiguration());
				}
			});
		}
	}

	private void loadScanResult() {

		log.fine("updating last scan result...");

		if (currentScanResultRequest != null) {

			currentScanResultRequest.cancel();

			BusyIndicator.finishTask();

			log.fine("active last scan result cancelled");
		}

		BusyIndicator.startTask();

		getView().setScanResultState(ContentState.LOADING);

		currentScanResultRequest = libraryService.getLastResult(new AsyncCallback<ScanResultDto>() {

			@Override
			public void onSuccess(ScanResultDto aResult) {

				BusyIndicator.finishTask();

				currentScanResultRequest = null;

				getView().setScanResult(aResult);
				getView().setScanResultState(ContentState.LOADED);

				log.fine("last scan result updated successfully");
			}

			@Override
			public void onFailure(Throwable aCaught) {

				BusyIndicator.finishTask();

				currentScanResultRequest = null;

				getView().setScanResultState(ContentState.ERROR);

				log.log(Level.SEVERE, "could not update last scan result", aCaught);
			}
		});
	}

	private void loadConfiguration() {

		log.fine("updating configuration...");

		if (currentConfigRequest != null) {

			currentConfigRequest.cancel();

			BusyIndicator.finishTask();

			log.fine("active configuration request cancelled");
		}

		BusyIndicator.startTask();

		getView().setConfigurationState(MyView.ConfigurationState.LOADING);

		currentConfigRequest = configurationService.getAll(new AsyncCallback<List<ConfigurationDto>>() {

			@Override
			public void onSuccess(List<ConfigurationDto> aResult) {

				BusyIndicator.finishTask();

				currentConfigRequest = null;

				getView().setConfiguration(aResult);
				getView().setConfigurationState(MyView.ConfigurationState.LOADED);

				log.fine("configuration updated successfully");
			}

			@Override
			public void onFailure(Throwable aCaught) {

				BusyIndicator.finishTask();

				currentConfigRequest = null;

				getView().setConfigurationState(MyView.ConfigurationState.ERROR);

				log.log(Level.SEVERE, "could not update configuration", aCaught);
			}
		});
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
