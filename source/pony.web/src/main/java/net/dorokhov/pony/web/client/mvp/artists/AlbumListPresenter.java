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
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.event.*;
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

public class AlbumListPresenter extends PresenterWidget<AlbumListPresenter.MyView> implements AlbumListUiHandlers, ArtistEvent.Handler, RefreshEvent.Handler, PlaybackEvent.Handler, SongEvent.Handler {

	public interface MyView extends View, HasUiHandlers<AlbumListUiHandlers>, HasContentState {

		public ArtistDto getArtist();

		public void setArtist(ArtistDto aArtist);

		public List<AlbumSongsDto> getAlbums();

		public void setAlbums(List<AlbumSongsDto> aAlbums);

		public SongDto getSelectedSong();

		public void setSelectedSong(SongDto aSong);

		public SongDto getActiveSong();

		public void setActiveSong(SongDto aSong);

		public boolean isPlaying();

		public void setPlaying(boolean aPlaying);

		public void scrollToTop();

		public void scrollToSong(SongDto aSong);

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final AlbumServiceRpcAsync albumService;

	private Request currentRequest;

	private boolean shouldHandleSongActivation = true;
	private boolean shouldScrollToSong = false;

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

		addRegisteredHandler(ArtistEvent.ARTIST_UPDATED, this);
		addRegisteredHandler(RefreshEvent.REFRESH_REQUESTED, this);
		addRegisteredHandler(PlaybackEvent.PLAYBACK_REQUESTED, this);
		addRegisteredHandler(SongEvent.SONG_CHANGED, this);
		addRegisteredHandler(SongEvent.SONG_STARTED, this);
		addRegisteredHandler(SongEvent.SONG_PAUSED, this);
		addRegisteredHandler(SongEvent.SONG_SELECTION_REQUESTED, this);
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
	public void onArtistEvent(ArtistEvent aEvent) {
		if (aEvent.getAssociatedType() == ArtistEvent.ARTIST_UPDATED) {

			if (getView().getArtist() != null && aEvent.hasArtist(getView().getArtist())) {
				getView().setArtist(aEvent.getArtist(getView().getArtist().getId()));
			}
		}
	}

	@Override
	public void onRefreshEvent(RefreshEvent aEvent) {
		if (getView().getArtist() != null) {
			doUpdateAlbums(getView().getArtist(), false);
		}
	}

	@Override
	public void onPlaybackEvent(PlaybackEvent aEvent) {

		SongDto song = getView().getSelectedSong();

		if (song == null) {

			AlbumSongsDto album = getView().getAlbums() != null && getView().getAlbums().size() > 0 ? getView().getAlbums().get(0) : null;

			if (album != null) {
				song = album.getSongs().size() > 0 ? album.getSongs().get(0) : null;
			}
		}

		if (song != null) {
			getView().setActiveSong(song);
		}
	}

	@Override
	public void onSongEvent(SongEvent aEvent) {
		if (aEvent.getAssociatedType() == SongEvent.SONG_CHANGED || aEvent.getAssociatedType() == SongEvent.SONG_STARTED) {

			if (!aEvent.getSong().equals(getView().getActiveSong())) {

				shouldHandleSongActivation = false;

				getView().setActiveSong(aEvent.getSong());
			}

			getView().setPlaying(aEvent.getAssociatedType() == SongEvent.SONG_STARTED);

		} else if (aEvent.getAssociatedType() == SongEvent.SONG_SELECTION_REQUESTED) {

			getView().setSelectedSong(aEvent.getSong());
			getView().scrollToSong(aEvent.getSong());

		} else if (aEvent.getAssociatedType() == SongEvent.SONG_PAUSED || aEvent.getAssociatedType() == SongEvent.SONG_FAILED) {
			getView().setPlaying(false);
		}
	}

	private void doUpdateAlbums(ArtistDto aArtist, boolean aShouldShowLoadingState) {

		ArtistDto oldArtist = getView().getArtist();

		getView().setArtist(aArtist);

		if (!ObjectUtils.nullSafeEquals(aArtist, oldArtist)) {

			getView().scrollToTop();

			shouldScrollToSong = true;
		}

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

					if (shouldScrollToSong) {

						if (getView().getSelectedSong() != null) {
							getView().scrollToSong(getView().getSelectedSong());
						} else if (getView().getActiveSong() != null) {
							getView().scrollToSong(getView().getActiveSong());
						}

						shouldScrollToSong = false;
					}
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
