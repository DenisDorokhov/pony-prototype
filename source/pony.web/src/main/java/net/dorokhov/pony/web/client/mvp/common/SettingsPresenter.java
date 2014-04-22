package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import net.dorokhov.pony.web.client.service.LibraryScanner;
import net.dorokhov.pony.web.shared.StatusDto;

public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> implements LibraryScanner.Delegate, SettingsUiHandlers {

	public interface MyView extends PopupView, HasUiHandlers<SettingsUiHandlers> {

		public static enum State {
			INACTIVE, SCAN_STARTING, SCANNING
		}

		public StatusDto getProgress();

		public void setProgress(StatusDto aProgress);

		public State getState();

		public void setState(State aState);
	}

	private final LibraryScanner libraryScanner;

	@Inject
	public SettingsPresenter(EventBus aEventBus, MyView aView, LibraryScanner aLibraryScanner) {

		super(aEventBus, aView);

		libraryScanner = aLibraryScanner;

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
	public void onScanStarted(LibraryScanner aLibraryScanner) {
		getView().setState(MyView.State.SCAN_STARTING);
	}

	@Override
	public void onScanProgress(LibraryScanner aLibraryScanner, StatusDto aStatus) {

		getView().setProgress(aStatus);

		getView().setState(MyView.State.SCANNING);
	}

	@Override
	public void onScanFailed(LibraryScanner aLibraryScanner) {

		getView().setState(MyView.State.INACTIVE);

		Window.alert("Could not start scanning!");
	}

	@Override
	public void onScanFinished(LibraryScanner aLibraryScanner) {
		getView().setState(MyView.State.INACTIVE);
	}

	@Override
	public void onScanRequested() {
		libraryScanner.scan();
	}
}
