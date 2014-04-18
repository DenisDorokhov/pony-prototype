package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class SearchView extends ViewImpl implements SearchPresenter.MyView {

	interface MyUiBinder extends UiBinder<Widget, SearchView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	public SearchView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
