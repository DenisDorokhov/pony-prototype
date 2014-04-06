package net.dorokhov.pony.web.client.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.place.ArtistsPlace;
import net.dorokhov.pony.web.client.presenter.impl.ArtistsActivity;

public class ArtistsActivityMapper implements ActivityMapper {

	private ArtistsActivity activity;

	@Inject
	public void setActivity(ArtistsActivity aActivity) {
		activity = aActivity;
	}

	@Override
	public Activity getActivity(Place aPlace) {

		if (aPlace instanceof ArtistsPlace) {
			return activity;
		}

		return null;
	}

}