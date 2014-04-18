package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.common.HasContentState;
import net.dorokhov.pony.web.client.event.SongPlaybackEvent;
import net.dorokhov.pony.web.client.service.AlbumServiceAsync;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlbumListPresenter extends PresenterWidget<AlbumListPresenter.MyView> implements AlbumListUiHandlers {

	public interface MyView extends View, HasUiHandlers<AlbumListUiHandlers>, HasContentState {

		public ArtistDto getArtist();

		public void setArtist(ArtistDto aArtist);

		public List<AlbumSongsDto> getAlbums();

		public void setAlbums(List<AlbumSongsDto> aAlbums);

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final AlbumServiceAsync albumService;

	private Request currentRequest;

	@Inject
	public AlbumListPresenter(EventBus aEventBus, AlbumListPresenter.MyView aView, AlbumServiceAsync aAlbumService) {

		super(aEventBus, aView);

		albumService = aAlbumService;

		getView().setUiHandlers(this);
	}

	public void loadArtist(ArtistDto aArtist) {

		getView().setArtist(aArtist);

		log.fine("updating albums of artist " + aArtist + "...");

		getView().setContentState(ContentState.LOADING);

		if (currentRequest != null) {

			currentRequest.cancel();

			log.fine("active albums request cancelled");
		}

		if (aArtist != null && aArtist.getId() != null) {

			currentRequest = albumService.getByArtist(aArtist.getId(), new AsyncCallback<ArrayList<AlbumSongsDto>>() {

				@Override
				public void onSuccess(ArrayList<AlbumSongsDto> aResult) {

					currentRequest = null;

					getView().setAlbums(aResult);
					getView().setContentState(ContentState.LOADED);

					log.fine("albums updated");
				}

				@Override
				public void onFailure(Throwable aCaught) {

					currentRequest = null;

					getView().setContentState(ContentState.ERROR);

					log.log(Level.SEVERE, "could not update albums", aCaught);

					Window.alert(aCaught.getMessage());
				}
			});

		} else {
			log.fine("albums cleared");
		}
	}

	@Override
	public void onSongSelected(SongDto aSong) {
		log.fine("song " + aSong + " selected");
	}

	@Override
	public void onSongActivated(SongDto aSong) {

		log.fine("song " + aSong + " activated");

		getEventBus().fireEvent(new SongPlaybackEvent(SongPlaybackEvent.PLAYBACK_REQUESTED, aSong));
	}
}
