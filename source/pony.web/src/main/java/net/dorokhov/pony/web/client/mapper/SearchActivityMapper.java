package net.dorokhov.pony.web.client.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.activity.SearchActivity;

public class SearchActivityMapper implements ActivityMapper {

	private SearchActivity activity;

	@Inject
	public void setActivity(SearchActivity aActivity) {
		activity = aActivity;
	}

	@Override
	public Activity getActivity(Place aPlace) {
		return activity;
	}

}
