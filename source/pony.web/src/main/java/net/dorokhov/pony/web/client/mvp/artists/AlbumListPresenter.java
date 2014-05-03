package net.dorokhov.pony.web.client.mvp.artists;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.common.HasContentState;
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

public class AlbumListPresenter extends PresenterWidget<AlbumListPresenter.MyView> implements AlbumListUiHandlers, RefreshEvent.Handler, SongEvent.Handler {

	public interface MyView extends View, HasUiHandlers<AlbumListUiHandlers>, HasContentState {

		public ArtistDto getArtist();

		public void setArtist(ArtistDto aArtist);

		public List<AlbumSongsDto> getAlbums();

		public void setAlbums(List<AlbumSongsDto> aAlbums);

		public SongDto getActiveSong();

		public void setActiveSong(SongDto aSong);

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final AlbumServiceRpcAsync albumService;

	private Request currentRequest;

	private boolean shouldHandleSongActivation = true;

	@Inject
	public AlbumListPresenter(EventBus aEventBus, AlbumListPresenter.MyView aView, AlbumServiceRpcAsync aAlbumService) {

		super(aEventBus, aView);

		albumService = aAlbumService;

		getView().setUiHandlers(this);
		getView().setContentState(ContentState.LOADING);
	}

	public void updateAlbums(ArtistDto aArtist) {
		doUpdateAlbums(aArtist, true);
	}

	@Override
	protected void onBind() {

		super.onBind();

		addRegisteredHandler(RefreshEvent.REFRESH_REQUESTED, this);
		addRegisteredHandler(SongEvent.SONG_STARTED, this);
	}

	@Override
	public void onSongSelection(SongDto aSong) {

		log.fine("song " + aSong + " selected");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_SELECTED, aSong));
	}

	@Override
	public void onSongActivation(SongDto aSong) {

		if (shouldHandleSongActivation) {

			log.fine("song " + aSong + " activated");

			List<SongDto> songs = new ArrayList<SongDto>();

			for (AlbumSongsDto album : getView().getAlbums()) {
				songs.addAll(album.getSongs());
			}

			getEventBus().fireEvent(new PlayListEvent(PlayListEvent.PLAYLIST_CHANGE, new PlayListImpl(songs), songs.indexOf(aSong)));
		}

		shouldHandleSongActivation = true;
	}

	@Override
	public void onRefreshEvent(RefreshEvent aEvent) {
		if (getView().getArtist() != null) {
			doUpdateAlbums(getView().getArtist(), false);
		}
	}

	@Override
	public void onSongEvent(SongEvent aEvent) {
		if (!aEvent.getSong().equals(getView().getActiveSong())) {

			shouldHandleSongActivation = false;

			getView().setActiveSong(aEvent.getSong());
		}
	}

	private void doUpdateAlbums(ArtistDto aArtist, boolean aShouldShowLoadingState) {

		getView().setArtist(aArtist);

		if (aArtist != null) {
			log.fine("updating albums of artist " + aArtist + "...");
		}

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

					getView().setContentState(ContentState.ERROR);

					log.log(Level.SEVERE, "could not update albums", aCaught);
				}
			});

		} else {

			getView().setContentState(ContentState.LOADED);

			log.fine("no albums can be loaded for empty artist");
		}
	}
}
