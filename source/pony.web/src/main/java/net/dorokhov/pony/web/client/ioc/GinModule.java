package net.dorokhov.pony.web.client.ioc;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import net.dorokhov.pony.web.client.mapper.ContentActivityMapper;
import net.dorokhov.pony.web.client.mapper.LogoActivityMapper;
import net.dorokhov.pony.web.client.mapper.PlayerActivityMapper;
import net.dorokhov.pony.web.client.mapper.SearchActivityMapper;
import net.dorokhov.pony.web.client.activity.ArtistsActivity;
import net.dorokhov.pony.web.client.activity.LogoActivity;
import net.dorokhov.pony.web.client.activity.PlayerActivity;
import net.dorokhov.pony.web.client.activity.SearchActivity;
import net.dorokhov.pony.web.client.service.*;
import net.dorokhov.pony.web.client.view.*;
import net.dorokhov.pony.web.client.view.impl.*;

public class GinModule extends AbstractGinModule {

	@Override
	public void configure() {

		bind(ArtistServiceAsync.class).in(Singleton.class);
		bind(AlbumServiceAsync.class).in(Singleton.class);
		bind(SongServiceAsync.class).in(Singleton.class);
		bind(SearchServiceAsync.class).in(Singleton.class);
		bind(LibraryServiceAsync.class).in(Singleton.class);

		bind(LogoActivity.class);
		bind(PlayerActivity.class);
		bind(SearchActivity.class);
		bind(ArtistsActivity.class);

		bind(LogoActivityMapper.class);
		bind(PlayerActivityMapper.class);
		bind(SearchActivityMapper.class);
		bind(ContentActivityMapper.class);

		bind(MainView.class).to(MainViewImpl.class);
		bind(LogoView.class).to(LogoViewImpl.class);
		bind(PlayerView.class).to(PlayerViewImpl.class);
		bind(SearchView.class).to(SearchViewImpl.class);
		bind(ArtistsView.class).to(ArtistsViewImpl.class);

		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
	}

	@Provides
	@Singleton
	public PlaceController getPlaceController(EventBus eventBus) {
		return new PlaceController(eventBus);
	}
}
