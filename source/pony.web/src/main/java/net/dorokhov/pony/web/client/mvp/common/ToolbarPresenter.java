package net.dorokhov.pony.web.client.mvp.common;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.dorokhov.pony.web.client.event.NoDataEvent;
import net.dorokhov.pony.web.client.event.RefreshEvent;
import net.dorokhov.pony.web.client.service.LibraryScanner;

public class ToolbarPresenter extends PresenterWidget<ToolbarPresenter.MyView> implements ToolbarUiHandlers, NoDataEvent.Handler {

	public interface MyView extends View, HasUiHandlers<ToolbarUiHandlers> {}

	private final LibraryScanner libraryScanner;

	private final SettingsPresenter settingsPresenter;

	@Inject
	public ToolbarPresenter(EventBus aEventBus, MyView aView, LibraryScanner aLibraryScanner, SettingsPresenter aPresenter) {

		super(aEventBus, aView);

		libraryScanner = aLibraryScanner;
		settingsPresenter = aPresenter;

		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {

		super.onBind();

		addRegisteredHandler(NoDataEvent.NO_DATA_DETECTED, this);
	}

	@Override
	public void onRefreshRequested() {
		getEventBus().fireEvent(new RefreshEvent(RefreshEvent.REFRESH_REQUESTED));
	}

	@Override
	public void onSettingsRequested() {
		showSettings();
	}

	@Override
	public void onNoDataEvent(NoDataEvent aEvent) {
		if (!libraryScanner.isScanning()) {
			showSettings();
		}
	}

	private void showSettings() {
		if (!settingsPresenter.isVisible()) {
			addToPopupSlot(settingsPresenter);
		}
	}
}
