package net.dorokhov.pony.web.client.presenter.impl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.presenter.ArtistsPresenter;
import net.dorokhov.pony.web.client.service.AlbumServiceAsync;
import net.dorokhov.pony.web.client.service.ArtistServiceAsync;
import net.dorokhov.pony.web.client.view.ArtistsView;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArtistsActivity extends AbstractActivity implements ArtistsPresenter {

	private final Logger log = Logger.getLogger(getClass().getName());

	private ArtistServiceAsync artistService;

	private AlbumServiceAsync albumService;

	private ArtistsView view;

	private String selectedArtistIdOrName;

	@Inject
	public void setArtistService(ArtistServiceAsync aArtistService) {
		artistService = aArtistService;
	}

	@Inject
	public void setAlbumService(AlbumServiceAsync aAlbumService) {
		albumService = aAlbumService;
	}

	@Inject
	public void setView(ArtistsView aView) {
		view = aView;
	}

	public String getSelectedArtistIdOrName() {
		return selectedArtistIdOrName;
	}

	public void setSelectedArtistIdOrName(String aSelectedArtistIdOrName) {
		selectedArtistIdOrName = aSelectedArtistIdOrName;
	}

	@Override
	public void start(AcceptsOneWidget aPanel, EventBus aEventBus) {

		view.setPresenter(this);

		view.setArtistsContentState(ContentState.LOADING);
		view.setAlbumsContentState(ContentState.LOADING);

		aPanel.setWidget(view);

		updateArtists();
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onArtistSelected(ArtistDto aArtist) {
		updateAlbums();
	}

	@Override
	public void onSongSelected(SongDto aSong) {
		log.fine("song [" + aSong + "] selected");
	}

	@Override
	public void onSongActivated(SongDto aSong) {
		log.fine("song [" + aSong + "] activated");
	}

	private void updateArtists() {

		log.fine("updating artists...");

		artistService.getAll(new AsyncCallback<ArrayList<ArtistDto>>() {

			@Override
			public void onSuccess(ArrayList<ArtistDto> aResult) {

				view.setArtists(aResult);

				view.setArtistsContentState(ContentState.LOADED);

				log.fine("artists updated");
			}

			@Override
			public void onFailure(Throwable aCaught) {

				view.setArtistsContentState(ContentState.ERROR);
				view.setAlbumsContentState(ContentState.ERROR);

				log.log(Level.SEVERE, "could not update artists", aCaught);

				Window.alert(aCaught.getMessage());
			}
		});
	}

	private void updateAlbums() {

		log.fine("updating albums...");

		albumService.getByArtist(view.getSelectedArtist().getId(), new AsyncCallback<ArrayList<AlbumSongsDto>>() {

			@Override
			public void onSuccess(ArrayList<AlbumSongsDto> aResult) {

				view.setAlbums(aResult);

				view.setAlbumsContentState(ContentState.LOADED);

				log.fine("albums updated");
			}

			@Override
			public void onFailure(Throwable aCaught) {

				view.setAlbumsContentState(ContentState.ERROR);

				log.log(Level.SEVERE, "could not update albums", aCaught);

				Window.alert(aCaught.getMessage());
			}
		});
	}
}
