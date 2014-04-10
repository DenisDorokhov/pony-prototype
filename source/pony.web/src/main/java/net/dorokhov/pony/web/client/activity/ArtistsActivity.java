package net.dorokhov.pony.web.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.event.ArtistSelectionEvent;
import net.dorokhov.pony.web.client.place.ArtistsPlace;
import net.dorokhov.pony.web.client.service.AlbumServiceAsync;
import net.dorokhov.pony.web.client.service.ArtistServiceAsync;
import net.dorokhov.pony.web.client.view.ArtistsView;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArtistsActivity extends AbstractActivity implements ArtistSelectionEvent.Handler {

	private final Logger log = Logger.getLogger(getClass().getName());

	private final Map<String, ArtistDto> artistMap = new HashMap<String, ArtistDto>();

	private PlaceController placeController;

	private ArtistServiceAsync artistService;

	private AlbumServiceAsync albumService;

	private ArtistsView view;

	private String artistToSelect;

	private Request currentArtistsRequest;
	private Request currentAlbumsRequest;

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

	@Override
	public void start(AcceptsOneWidget aPanel, EventBus aEventBus) {

		aEventBus.addHandler(ArtistSelectionEvent.TYPE, this);

		view.setEventBus(aEventBus);

		view.setArtistsContentState(ContentState.LOADING);
		view.setAlbumsContentState(ContentState.LOADING);

		aPanel.setWidget(view);

		updateArtists();
	}

	@Override
	public void onStop() {
		view.setEventBus(null);
	}

	@Override
	public void onArtistSelection(ArtistSelectionEvent aEvent) {

		updateAlbums();

		goToArtist(aEvent.getArtist());
	}

	public void setArtistToSelect(String aArtistToSelect) {

		artistToSelect = aArtistToSelect;

		selectArtist(artistToSelect);
	}

	private void updateArtists() {

		log.fine("updating artists...");

		if (currentArtistsRequest != null) {

			currentArtistsRequest.cancel();

			log.fine("active artists request cancelled");
		}

		currentArtistsRequest = artistService.getAll(new AsyncCallback<ArrayList<ArtistDto>>() {

			@Override
			public void onSuccess(ArrayList<ArtistDto> aResult) {

				currentArtistsRequest = null;

				doUpdateArtists(aResult);

				view.setArtistsContentState(ContentState.LOADED);

				log.fine("artists updated");

				if (aResult.size() > 0) {
					selectArtist(artistToSelect);
				} else {
					view.setAlbumsContentState(ContentState.LOADED);
				}
			}

			@Override
			public void onFailure(Throwable aCaught) {

				currentArtistsRequest = null;

				doUpdateArtists(new ArrayList<ArtistDto>());

				view.setArtistsContentState(ContentState.ERROR);
				view.setAlbumsContentState(ContentState.ERROR);

				log.log(Level.SEVERE, "could not update artists", aCaught);

				Window.alert(aCaught.getMessage());
			}
		});
	}

	private void doUpdateArtists(List<ArtistDto> aArtists) {

		for (String key : artistMap.keySet()) {
			artistMap.remove(key);
		}

		for (ArtistDto artist : aArtists) {
			if (artist.getName() != null) {
				artistMap.put(artist.getName().toLowerCase(), artist);
			}
		}
		// Identifier has higher priority, name keys must be rewritten
		for (ArtistDto artist : aArtists) {
			if (artist.getId() != null) {
				artistMap.put(artist.getId().toString(), artist);
			}
		}

		view.setArtists(aArtists);
	}

	private void updateAlbums() {

		log.fine("updating albums of artist [" + view.getSelectedArtist().getName() + "]...");

		view.setAlbumsContentState(ContentState.LOADING);

		if (currentAlbumsRequest != null) {

			currentAlbumsRequest.cancel();

			log.fine("active albums request cancelled");
		}

		currentAlbumsRequest = albumService.getByArtist(view.getSelectedArtist().getId(), new AsyncCallback<ArrayList<AlbumSongsDto>>() {

			@Override
			public void onSuccess(ArrayList<AlbumSongsDto> aResult) {

				currentAlbumsRequest = null;

				view.setAlbums(aResult);

				view.setAlbumsContentState(ContentState.LOADED);

				log.fine("albums updated");
			}

			@Override
			public void onFailure(Throwable aCaught) {

				currentAlbumsRequest = null;

				view.setAlbumsContentState(ContentState.ERROR);

				log.log(Level.SEVERE, "could not update albums", aCaught);

				Window.alert(aCaught.getMessage());
			}
		});
	}

	private void selectArtist(String aArtist) {

		List<ArtistDto> artists = view.getArtists();

		if (artists != null && artists.size() > 0) {

			ArtistDto artistToSelect = findArtist(aArtist);

			if (artistToSelect != null) {
				view.setSelectedArtist(artistToSelect);
			} else if (aArtist != null && !aArtist.trim().equals("")) {
				log.warning("could not find artist [" + aArtist + "]");
			}

			if (view.getSelectedArtist() == null) {
				goToArtist(artists.get(0));
			}
		}
	}

	private ArtistDto findArtist(String aArtist) {

		if (aArtist != null) {

			String artistName = aArtist.toLowerCase();

			return artistMap.get(artistName);
		}

		return null;
	}

	private void goToArtist(ArtistDto aArtist) {
		placeController.goTo(new ArtistsPlace(aArtist.getName()));
	}
}
