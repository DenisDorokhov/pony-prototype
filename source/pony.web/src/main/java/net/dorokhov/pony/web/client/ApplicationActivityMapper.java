package net.dorokhov.pony.web.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import net.dorokhov.pony.web.client.ioc.Injector;
import net.dorokhov.pony.web.client.place.AlbumListPlace;
import net.dorokhov.pony.web.client.place.ArtistListPlace;
import net.dorokhov.pony.web.client.presenter.impl.AlbumListActivity;

public class ApplicationActivityMapper implements ActivityMapper {

	private final Injector injector = GWT.create(Injector.class);

	@Override
	public Activity getActivity(Place aPlace) {

		if (aPlace instanceof ArtistListPlace) {

			return injector.getArtistListPresenter();

		} else if (aPlace instanceof AlbumListPlace) {

			AlbumListPlace place = (AlbumListPlace)aPlace;

			AlbumListActivity activity = injector.getAlbumListPresenter();

			activity.setArtistIdOrName(place.getArtistIdOrName());

			return activity;
		}

		return null;
	}

}
