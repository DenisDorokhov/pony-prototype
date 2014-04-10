package net.dorokhov.pony.web.client.mapper;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.activity.LogoActivity;

public class LogoActivityMapper implements ActivityMapper {

	private LogoActivity activity;

	@Inject
	public void setActivity(LogoActivity aActivity) {
		activity = aActivity;
	}

	@Override
	public Activity getActivity(Place aPlace) {
		return activity;
	}

}
