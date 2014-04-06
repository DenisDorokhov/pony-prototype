package net.dorokhov.pony.web.client.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.presenter.impl.PlayerActivity;

public class PlayerActivityMapper implements ActivityMapper {

	private PlayerActivity activity;

	@Inject
	public void setActivity(PlayerActivity aActivity) {
		activity = aActivity;
	}

	@Override
	public Activity getActivity(Place aPlace) {
		return activity;
	}

}
