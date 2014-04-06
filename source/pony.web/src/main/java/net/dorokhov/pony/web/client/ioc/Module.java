package net.dorokhov.pony.web.client.ioc;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import net.dorokhov.pony.web.client.presenter.impl.AlbumListActivity;
import net.dorokhov.pony.web.client.presenter.impl.ArtistListActivity;
import net.dorokhov.pony.web.client.service.*;

public class Module extends AbstractGinModule {

	@Override
	public void configure() {

		bind(ArtistListActivity.class);
		bind(AlbumListActivity.class);

		bind(ArtistServiceAsync.class).in(Singleton.class);
		bind(AlbumServiceAsync.class).in(Singleton.class);
		bind(SongServiceAsync.class).in(Singleton.class);
		bind(SearchServiceAsync.class).in(Singleton.class);
		bind(LibraryServiceAsync.class).in(Singleton.class);

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	public PlaceController getPlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}
}
