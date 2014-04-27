package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class LogoView extends ViewImpl implements LogoPresenter.MyView {

	interface MyUiBinder extends UiBinder<Widget, LogoView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	public LogoView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
