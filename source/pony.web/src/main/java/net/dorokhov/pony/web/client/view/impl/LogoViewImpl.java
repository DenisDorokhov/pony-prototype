package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.presenter.LogoPresenter;
import net.dorokhov.pony.web.client.view.LogoView;

public class LogoViewImpl extends Composite implements LogoView {

	interface LogoViewUiBinder extends UiBinder<Widget, LogoViewImpl> {}

	private static LogoViewUiBinder uiBinder = GWT.create(LogoViewUiBinder.class);

	private LogoPresenter presenter;

	public LogoViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(LogoPresenter aPresenter) {
		presenter = aPresenter;
	}
}
