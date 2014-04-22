package net.dorokhov.pony.web.client.mvp.common;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.dorokhov.pony.web.client.event.RefreshEvent;

public class ToolbarPresenter extends PresenterWidget<ToolbarPresenter.MyView> implements ToolbarUiHandlers {

	public interface MyView extends View, HasUiHandlers<ToolbarUiHandlers> {}

	private final SettingsPresenter settingsPresenter;

	@Inject
	public ToolbarPresenter(EventBus aEventBus, MyView aView, SettingsPresenter aPresenter) {

		super(aEventBus, aView);

		settingsPresenter = aPresenter;

		getView().setUiHandlers(this);
	}

	@Override
	public void onRefreshRequested() {
		getEventBus().fireEvent(new RefreshEvent(RefreshEvent.REFRESH_SELECTED));
	}

	@Override
	public void onSettingsRequested() {
		addToPopupSlot(settingsPresenter);
	}
}
