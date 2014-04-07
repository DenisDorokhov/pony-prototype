package net.dorokhov.pony.web.client.presenter.impl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.place.ArtistsPlace;
import net.dorokhov.pony.web.client.presenter.ArtistsPresenter;
import net.dorokhov.pony.web.client.service.AlbumServiceAsync;
import net.dorokhov.pony.web.client.service.ArtistServiceAsync;
import net.dorokhov.pony.web.client.view.ArtistsView;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArtistsActivity extends AbstractActivity implements ArtistsPresenter {

	private final Logger log = Logger.getLogger(getClass().getName());

	private PlaceController placeController;

	private ArtistServiceAsync artistService;

	private AlbumServiceAsync albumService;

	private ArtistsView view;

	private String selectedArtistIdOrName;

	@Inject
	public void setPlaceController(PlaceController aPlaceController) {
		placeController = aPlaceController;
	}

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

		selectArtist(selectedArtistIdOrName);
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

		placeController.goTo(new ArtistsPlace(aArtist.getName()));

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

				selectArtist(selectedArtistIdOrName);
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

		view.setAlbumsContentState(ContentState.LOADING);

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

	private void selectArtist(String aArtistIdOrName) {

		List<ArtistDto> artists = view.getArtists();

		if (artists != null && artists.size() > 0) {

			ArtistDto artistToSelect = findArtist(aArtistIdOrName, artists);

			if (artistToSelect == null) {
				artistToSelect = artists.get(0);
			}

			view.setSelectedArtist(artistToSelect);
		}
	}

	private ArtistDto findArtist(String aArtistIdOrName, List<ArtistDto> aArtists) {

		if (aArtistIdOrName != null) {

			String artistName = aArtistIdOrName.trim();

			Integer artistId = null;
			try {
				artistId = new Integer(artistName);
			} catch (NumberFormatException e) {}

			for (ArtistDto artist : aArtists) {
				if (artist.getId() != null && artist.getId().equals(artistId)) {
					return artist;
				} else  if (artist.getName() != null && artist.getName().equals(artistName)) {
					return artist;
				}
			}
		}

		return null;
	}
}
