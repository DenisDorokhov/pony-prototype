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
import net.dorokhov.pony.web.client.event.ArtistEvent;
import net.dorokhov.pony.web.client.service.ArtistServiceAsync;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArtistListPresenter extends PresenterWidget<ArtistListPresenter.MyView> implements ArtistListUiHandlers {

	public interface MyView extends View, HasUiHandlers<ArtistListUiHandlers>, HasContentState {

		public List<ArtistDto> getArtists();

		public void setArtists(List<ArtistDto> aArtists);

		public ArtistDto getSelectedArtist();

		public void setSelectedArtist(ArtistDto aArtist);

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final ArtistServiceAsync artistService;

	private final HashMap<String, ArtistDto> artistMap = new HashMap<String, ArtistDto>();

	private String artistToSelect;

	private Request currentRequest;

	@Inject
	public ArtistListPresenter(EventBus aEventBus, MyView aView, ArtistServiceAsync aArtistService) {

		super(aEventBus, aView);

		artistService = aArtistService;

		getView().setUiHandlers(this);
	}

	public void selectArtist(String aArtist) {

		artistToSelect = aArtist;

		doSelectArtist(artistToSelect);
	}

	@Override
	protected void onReveal() {

		super.onReveal();

		log.fine("updating artists...");

		getView().setContentState(ContentState.LOADING);

		if (currentRequest != null) {

			currentRequest.cancel();

			log.fine("active artists request cancelled");
		}

		currentRequest = artistService.getAll(new AsyncCallback<ArrayList<ArtistDto>>() {

			@Override
			public void onSuccess(ArrayList<ArtistDto> aResult) {

				currentRequest = null;

				doUpdateArtists(aResult);

				getView().setContentState(ContentState.LOADED);

				log.fine("artists updated");

				doSelectArtist(artistToSelect);
			}

			@Override
			public void onFailure(Throwable aCaught) {

				currentRequest = null;

				doUpdateArtists(new ArrayList<ArtistDto>());

				getView().setContentState(ContentState.ERROR);

				log.log(Level.SEVERE, "could not update artists", aCaught);

				Window.alert(aCaught.getMessage());
			}
		});
	}

	@Override
	public void onArtistSelection(ArtistDto aArtist) {
		getEventBus().fireEvent(new ArtistEvent(ArtistEvent.SELECTION, aArtist));
	}

	private void doUpdateArtists(List<ArtistDto> aArtists) {

		artistMap.clear();

		for (ArtistDto artist : aArtists) {
			if (artist.getName() != null) {
				artistMap.put(artist.getName().trim().toLowerCase(), artist);
			}
		}
		for (ArtistDto artist : aArtists) {
			if (artist.getId() != null) {
				artistMap.put(artist.getId().toString(), artist);
			}
		}

		getView().setArtists(aArtists);
	}

	private void doSelectArtist(String aArtist) {

		List<ArtistDto> artists = getView().getArtists();

		if (artists != null && artists.size() > 0) {

			ArtistDto artistToSelect = findArtist(aArtist);

			if (artistToSelect != null) {
				getView().setSelectedArtist(artistToSelect);
			} else if (aArtist != null && !aArtist.trim().equals("")) {
				log.warning("could not find artist [" + aArtist + "]");
			}

			if (getView().getSelectedArtist() == null) {
				getView().setSelectedArtist(artists.get(0));
			}
		}
	}

	private ArtistDto findArtist(String aArtist) {

		if (aArtist != null) {

			String artistName = aArtist.trim().toLowerCase();

			return artistMap.get(artistName);
		}

		return null;
	}
}
