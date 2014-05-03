package net.dorokhov.pony.web.client.mvp;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import net.dorokhov.pony.web.client.PlaceTokens;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.client.event.ArtistEvent;
import net.dorokhov.pony.web.client.event.NoDataEvent;
import net.dorokhov.pony.web.client.mvp.artists.AlbumListPresenter;
import net.dorokhov.pony.web.client.mvp.artists.ArtistListPresenter;
import net.dorokhov.pony.web.shared.ArtistDto;

public class ArtistsPresenter extends Presenter<ArtistsPresenter.MyView, ArtistsPresenter.MyProxy> implements ArtistEvent.Handler, NoDataEvent.Handler {

	@ProxyStandard
	@NameToken(PlaceTokens.TOKEN_ARTISTS)
	public interface MyProxy extends ProxyPlace<ArtistsPresenter> {}

	public interface MyView extends View {}

	public static final Object SLOT_ARTISTS = new Object();
	public static final Object SLOT_ALBUMS = new Object();

	private final PlaceManager placeManager;

	private final ArtistListPresenter artistListPresenter;
	private final AlbumListPresenter albumListPresenter;

	@Inject
	public ArtistsPresenter(EventBus aEventBus, MyView aView, MyProxy aProxy, PlaceManager aPlaceManager,
							ArtistListPresenter aArtistListPresenter, AlbumListPresenter aAlbumListPresenter) {

		super(aEventBus, aView, aProxy, ApplicationPresenter.SLOT_CONTENT);

		placeManager = aPlaceManager;

		artistListPresenter = aArtistListPresenter;
		albumListPresenter = aAlbumListPresenter;
	}

	@Override
	protected void onBind() {

		super.onBind();

		setInSlot(SLOT_ARTISTS, artistListPresenter);
		setInSlot(SLOT_ALBUMS, albumListPresenter);

		addRegisteredHandler(ArtistEvent.ARTIST_SELECTED, this);
		addRegisteredHandler(NoDataEvent.NO_DATA_DETECTED, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest aRequest) {

		super.prepareFromRequest(aRequest);

		artistListPresenter.selectArtist(aRequest.getParameter(PlaceTokens.PARAM_ARTIST, null));
	}

	@Override
	public void onArtistEvent(ArtistEvent aEvent) {

		albumListPresenter.updateAlbums(aEvent.getArtist());

		goToArtist(aEvent.getArtist());
	}

	@Override
	public void onNoDataEvent(NoDataEvent aEvent) {
		albumListPresenter.updateAlbums(null);
	}

	private void goToArtist(ArtistDto aArtist) {

		String artistName = aArtist != null ? aArtist.getName() : null;
		String artistId = aArtist != null ? ObjectUtils.nullSafeToString(aArtist.getId()) : null;

		PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();

		boolean nameTokenChanged = !StringUtils.nullSafeNormalizedEquals(
				currentPlaceRequest.getNameToken(), PlaceTokens.TOKEN_ARTISTS);

		boolean artistChanged =
				!StringUtils.nullSafeNormalizedEquals(
						currentPlaceRequest.getParameter(PlaceTokens.PARAM_ARTIST, null), artistName)
				&&
				!StringUtils.nullSafeNormalizedEquals(
						currentPlaceRequest.getParameter(PlaceTokens.PARAM_ARTIST, null), artistId);

		if (nameTokenChanged || artistChanged) {

			PlaceRequest.Builder builder = new PlaceRequest.Builder().nameToken(PlaceTokens.TOKEN_ARTISTS);

			if (artistName != null) {
				builder.with(PlaceTokens.PARAM_ARTIST, artistName.trim().toLowerCase());
			}

			placeManager.revealPlace(builder.build());
		}
	}
}
