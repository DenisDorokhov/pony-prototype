package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import net.dorokhov.pony.web.client.presenter.SearchPresenter;
import net.dorokhov.pony.web.client.view.SearchView;

public class SearchViewImpl extends Composite implements SearchView {

	interface SearchViewUiBinder extends UiBinder<HTMLPanel, SearchViewImpl> {}

	private static SearchViewUiBinder uiBinder = GWT.create(SearchViewUiBinder.class);

	private SearchPresenter presenter;

	public SearchViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(SearchPresenter aPresenter) {
		presenter = aPresenter;
	}
}
