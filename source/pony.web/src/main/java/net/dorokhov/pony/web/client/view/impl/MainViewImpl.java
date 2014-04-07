package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.view.MainView;

public class MainViewImpl extends Composite implements MainView {

	interface MainViewUiBinder extends UiBinder<Widget, MainViewImpl> {}

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	@UiField
	SimpleLayoutPanel logoContainer;

	@UiField
	SimpleLayoutPanel playerContainer;

	@UiField
	SimpleLayoutPanel searchContainer;

	@UiField
	SimpleLayoutPanel contentContainer;

	public MainViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public SimpleLayoutPanel getLogoContainer() {
		return logoContainer;
	}

	@Override
	public SimpleLayoutPanel getPlayerContainer() {
		return playerContainer;
	}

	@Override
	public SimpleLayoutPanel getSearchContainer() {
		return searchContainer;
	}

	@Override
	public SimpleLayoutPanel getContentContainer() {
		return contentContainer;
	}
}
