package net.dorokhov.pony.web.client;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.proxy.DefaultPlaceManager;
import net.dorokhov.pony.web.client.mvp.ApplicationPresenter;
import net.dorokhov.pony.web.client.mvp.ApplicationView;
import net.dorokhov.pony.web.client.mvp.ArtistsPresenter;
import net.dorokhov.pony.web.client.mvp.ArtistsView;
import net.dorokhov.pony.web.client.mvp.artist.AlbumListPresenter;
import net.dorokhov.pony.web.client.mvp.artist.AlbumListView;
import net.dorokhov.pony.web.client.mvp.artist.ArtistListPresenter;
import net.dorokhov.pony.web.client.mvp.artist.ArtistListView;
import net.dorokhov.pony.web.client.mvp.common.*;
import net.dorokhov.pony.web.client.service.rpc.*;

public class ApplicationModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		install(new DefaultModule(DefaultPlaceManager.class));

		bind(ArtistServiceAsync.class).in(Singleton.class);
		bind(AlbumServiceAsync.class).in(Singleton.class);
		bind(SongServiceAsync.class).in(Singleton.class);
		bind(SearchServiceAsync.class).in(Singleton.class);
		bind(LibraryServiceAsync.class).in(Singleton.class);

		bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class, ApplicationPresenter.MyProxy.class);

		bindSingletonPresenterWidget(LogoPresenter.class, LogoPresenter.MyView.class, LogoView.class);
		bindSingletonPresenterWidget(PlayerPresenter.class, PlayerPresenter.MyView.class, PlayerView.class);
		bindSingletonPresenterWidget(ToolbarPresenter.class, ToolbarPresenter.MyView.class, ToolbarView.class);

		bindPresenter(ArtistsPresenter.class, ArtistsPresenter.MyView.class, ArtistsView.class, ArtistsPresenter.MyProxy.class);

		bindSingletonPresenterWidget(ArtistListPresenter.class, ArtistListPresenter.MyView.class, ArtistListView.class);
		bindSingletonPresenterWidget(AlbumListPresenter.class, AlbumListPresenter.MyView.class, AlbumListView.class);

		bindConstant().annotatedWith(DefaultPlace.class).to(PlaceTokens.TOKEN_ARTISTS);
		bindConstant().annotatedWith(ErrorPlace.class).to(PlaceTokens.TOKEN_ARTISTS);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(PlaceTokens.TOKEN_ARTISTS);
	}
}
