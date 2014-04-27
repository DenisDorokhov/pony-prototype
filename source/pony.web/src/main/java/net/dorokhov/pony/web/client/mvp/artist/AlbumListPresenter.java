package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.common.HasContentState;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.event.PlayListEvent;
import net.dorokhov.pony.web.client.event.RefreshEvent;
import net.dorokhov.pony.web.client.event.SongEvent;
import net.dorokhov.pony.web.client.service.BusyIndicator;
import net.dorokhov.pony.web.client.service.PlayListImpl;
import net.dorokhov.pony.web.client.service.rpc.AlbumServiceRpcAsync;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlbumListPresenter extends PresenterWidget<AlbumListPresenter.MyView> implements AlbumListUiHandlers, RefreshEvent.Handler {

	public interface MyView extends View, HasUiHandlers<AlbumListUiHandlers>, HasContentState {

		public ArtistDto getArtist();

		public void setArtist(ArtistDto aArtist);

		public List<AlbumSongsDto> getAlbums();

		public void setAlbums(List<AlbumSongsDto> aAlbums);

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final AlbumServiceRpcAsync albumService;

	private Request currentRequest;

	@Inject
	public AlbumListPresenter(EventBus aEventBus, AlbumListPresenter.MyView aView, AlbumServiceRpcAsync aAlbumService) {

		super(aEventBus, aView);

		albumService = aAlbumService;

		getView().setUiHandlers(this);
	}

	public void loadArtist(ArtistDto aArtist) {
		if (!ObjectUtils.nullSafeEquals(getView().getArtist(), aArtist)) {
			doLoadArtist(aArtist, true);
		}
	}

	@Override
	protected void onBind() {

		super.onBind();

		addRegisteredHandler(RefreshEvent.REFRESH_REQUESTED, this);
	}

	@Override
	public void onSongSelection(SongDto aSong) {

		log.fine("song " + aSong + " selected");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_SELECTED, aSong));
	}

	@Override
	public void onSongActivation(SongDto aSong) {

		log.fine("song " + aSong + " playback requested");

		List<SongDto> songs = new ArrayList<SongDto>();

		for (AlbumSongsDto album : getView().getAlbums()) {
			songs.addAll(album.getSongs());
		}

		getEventBus().fireEvent(new PlayListEvent(PlayListEvent.PLAYLIST_CHANGE,
				new PlayListImpl(songs), songs.indexOf(aSong)));
	}

	@Override
	public void onRefreshEvent(RefreshEvent aEvent) {
		if (getView().getArtist() != null) {
			doLoadArtist(getView().getArtist(), false);
		}
	}

	private void doLoadArtist(ArtistDto aArtist, boolean aShouldShowLoadingState) {

		getView().setArtist(aArtist);

		log.fine("updating albums of artist " + aArtist + "...");

		if (aShouldShowLoadingState || getView().getContentState() != ContentState.LOADED) {
			getView().setContentState(ContentState.LOADING);
		}

		if (currentRequest != null) {

			currentRequest.cancel();

			BusyIndicator.finishTask();

			log.fine("active albums request cancelled");
		}

		if (aArtist != null && aArtist.getId() != null) {

			BusyIndicator.startTask();

			currentRequest = albumService.getByArtist(aArtist.getId(), new AsyncCallback<ArrayList<AlbumSongsDto>>() {

				@Override
				public void onSuccess(ArrayList<AlbumSongsDto> aResult) {

					BusyIndicator.finishTask();

					currentRequest = null;

					getView().setAlbums(aResult);
					getView().setContentState(ContentState.LOADED);

					log.fine("albums updated");
				}

				@Override
				public void onFailure(Throwable aCaught) {

					BusyIndicator.finishTask();

					currentRequest = null;

					if (getView().getContentState() == ContentState.LOADING) {
						getView().setContentState(ContentState.ERROR);
					}

					log.log(Level.SEVERE, "could not update albums", aCaught);
				}
			});

		} else {
			log.fine("albums cleared");
		}
	}
}
