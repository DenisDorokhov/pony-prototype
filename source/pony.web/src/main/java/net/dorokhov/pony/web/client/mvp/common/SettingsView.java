package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class SettingsView extends PopupViewImpl implements SettingsPresenter.MyView {

	interface MyUiBinder extends UiBinder<PopupPanel, SettingsView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@Inject
	public SettingsView(EventBus aEventBus) {

		super(aEventBus);

		initWidget(uiBinder.createAndBindUi(this));
	}
}
