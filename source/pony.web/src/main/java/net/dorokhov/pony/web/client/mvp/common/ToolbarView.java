package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ToolbarView extends ViewWithUiHandlers<ToolbarUiHandlers> implements ToolbarPresenter.MyView {

	interface MyUiBinder extends UiBinder<Widget, ToolbarView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	public ToolbarView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("refreshButton")
	void onRefreshClick(ClickEvent aEvent) {
		getUiHandlers().onRefreshRequested();
	}

	@UiHandler("settingsButton")
	void onSettingsClick(ClickEvent aEvent) {
		getUiHandlers().onSettingsRequested();
	}
}
