package net.dorokhov.pony.web.client.presenter.impl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.presenter.SearchPresenter;
import net.dorokhov.pony.web.client.view.SearchView;

public class SearchActivity extends AbstractActivity implements SearchPresenter {

	private SearchView view;

	@Inject
	public void setView(SearchView aView) {
		view = aView;
	}

	@Override
	public void start(AcceptsOneWidget aPanel, EventBus aEventBus) {

		view.setPresenter(this);

		aPanel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onSearchRequested() {
		// TODO: implement
	}
}
