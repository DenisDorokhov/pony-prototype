package net.dorokhov.pony.web.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.view.SearchView;

public class SearchActivity extends AbstractActivity {

	private SearchView view;

	@Inject
	public void setView(SearchView aView) {
		view = aView;
	}

	@Override
	public void start(AcceptsOneWidget aPanel, EventBus aEventBus) {
		aPanel.setWidget(view);
	}
}
