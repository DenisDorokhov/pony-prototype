package net.dorokhov.pony.web.client.mvp;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import net.dorokhov.pony.web.client.NameTokens;
import net.dorokhov.pony.web.client.mvp.artist.AlbumListPresenter;
import net.dorokhov.pony.web.client.mvp.artist.ArtistListPresenter;

public class ArtistsPresenter extends Presenter<ArtistsPresenter.MyView, ArtistsPresenter.MyProxy> {

	@ProxyStandard
	@NameToken(NameTokens.TOKEN_ARTISTS)
	public interface MyProxy extends ProxyPlace<ArtistsPresenter> {}

	public interface MyView extends View {}

	public static final Object SLOT_ARTISTS = new Object();
	public static final Object SLOT_ALBUMS = new Object();

	private final ArtistListPresenter artistListPresenter;
	private final AlbumListPresenter albumListPresenter;

	@Inject
	public ArtistsPresenter(EventBus aEventBus, MyView aView, MyProxy aProxy,
							ArtistListPresenter aArtistListPresenter, AlbumListPresenter aAlbumListPresenter) {

		super(aEventBus, aView, aProxy, ApplicationPresenter.SLOT_CONTENT);

		artistListPresenter = aArtistListPresenter;
		albumListPresenter = aAlbumListPresenter;
	}

	@Override
	protected void onBind() {

		super.onBind();

		setInSlot(SLOT_ARTISTS, artistListPresenter);
		setInSlot(SLOT_ALBUMS, albumListPresenter);
	}

	@Override
	public void prepareFromRequest(PlaceRequest aRequest) {

		super.prepareFromRequest(aRequest);

		artistListPresenter.selectArtist(aRequest.getParameter(NameTokens.PARAM_ARTIST, null));

		albumListPresenter.loadArtist(artistListPresenter.getSelectedArtist());
	}

	@Override
	protected void revealInParent() {
		super.revealInParent();
	}
}
