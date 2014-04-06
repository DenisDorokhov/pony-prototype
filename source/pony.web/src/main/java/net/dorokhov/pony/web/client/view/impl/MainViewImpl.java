package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import net.dorokhov.pony.web.client.view.MainView;

public class MainViewImpl extends Composite implements MainView {

	interface MainViewUiBinder extends UiBinder<DockLayoutPanel, MainViewImpl> {}

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	@UiField
	AcceptsOneWidget logoContainer;

	@UiField
	AcceptsOneWidget playerContainer;

	@UiField
	AcceptsOneWidget searchContainer;

	@UiField
	AcceptsOneWidget contentContainer;

	public MainViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public AcceptsOneWidget getLogoContainer() {
		return logoContainer;
	}

	@Override
	public AcceptsOneWidget getPlayerContainer() {
		return playerContainer;
	}

	@Override
	public AcceptsOneWidget getSearchContainer() {
		return searchContainer;
	}

	@Override
	public AcceptsOneWidget getContentContainer() {
		return contentContainer;
	}
}
