package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.view.SearchView;

public class SearchViewImpl extends Composite implements SearchView {

	interface SearchViewUiBinder extends UiBinder<Widget, SearchViewImpl> {}

	private static SearchViewUiBinder uiBinder = GWT.create(SearchViewUiBinder.class);

	public SearchViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
