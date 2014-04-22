package net.dorokhov.pony.web.client.mvp.common;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class SettingsPresenter extends PresenterWidget<SettingsPresenter.MyView> {

	public interface MyView extends PopupView {}

	@Inject
	public SettingsPresenter(EventBus aEventBus, MyView aView) {
		super(aEventBus, aView);
	}
}
