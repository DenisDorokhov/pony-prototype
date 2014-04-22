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
import net.dorokhov.pony.web.client.event.ArtistEvent;
import net.dorokhov.pony.web.client.event.RefreshEvent;
import net.dorokhov.pony.web.client.service.BusyIndicator;
import net.dorokhov.pony.web.client.service.rpc.ArtistServiceRpcAsync;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArtistListPresenter extends PresenterWidget<ArtistListPresenter.MyView> implements ArtistListUiHandlers, RefreshEvent.Handler {

	public interface MyView extends View, HasUiHandlers<ArtistListUiHandlers>, HasContentState {

		public List<ArtistDto> getArtists();

		public void setArtists(List<ArtistDto> aArtists);

		public ArtistDto getSelectedArtist();

		public void setSelectedArtist(ArtistDto aArtist, boolean aShouldScroll);

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final ArtistServiceRpcAsync artistService;

	private final HashMap<String, ArtistDto> artistMap = new HashMap<String, ArtistDto>();

	private String artistToSelect;

	private Request currentRequest;

	@Inject
	public ArtistListPresenter(EventBus aEventBus, MyView aView, ArtistServiceRpcAsync aArtistService) {

		super(aEventBus, aView);

		artistService = aArtistService;

		getView().setUiHandlers(this);
	}

	public void selectArtist(String aArtist) {

		artistToSelect = aArtist;

		doSelectArtist(artistToSelect, true);
	}

	@Override
	protected void onBind() {

		super.onBind();

		addRegisteredHandler(RefreshEvent.REFRESH_SELECTED, this);
	}

	@Override
	protected void onReveal() {

		super.onReveal();

		loadArtists(true, true);
	}

	@Override
	public void onArtistSelection(ArtistDto aArtist) {
		getEventBus().fireEvent(new ArtistEvent(ArtistEvent.ARTIST_SELECTED, aArtist));
	}

	@Override
	public void onRefreshEvent(RefreshEvent aEvent) {
		loadArtists(false, false);
	}

	private void loadArtists(boolean aShouldShowLoadingState, final boolean aShouldScroll) {

		log.fine("updating artists...");

		if (aShouldShowLoadingState || getView().getContentState() != ContentState.LOADED) {
			getView().setContentState(ContentState.LOADING);
		}

		if (currentRequest != null) {

			currentRequest.cancel();

			BusyIndicator.finishTask();

			log.fine("active artists request cancelled");
		}

		BusyIndicator.startTask();

		currentRequest = artistService.getAll(new AsyncCallback<ArrayList<ArtistDto>>() {

			@Override
			public void onSuccess(ArrayList<ArtistDto> aResult) {

				BusyIndicator.finishTask();

				currentRequest = null;

				doUpdateArtists(aResult);

				getView().setContentState(ContentState.LOADED);

				log.fine("artists updated");

				doSelectArtist(artistToSelect, aShouldScroll);
			}

			@Override
			public void onFailure(Throwable aCaught) {

				BusyIndicator.finishTask();

				currentRequest = null;

				doUpdateArtists(new ArrayList<ArtistDto>());

				if (getView().getContentState() == ContentState.LOADING) {
					getView().setContentState(ContentState.ERROR);
				}

				log.log(Level.SEVERE, "could not update artists", aCaught);
			}
		});
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

	private void doSelectArtist(String aArtist, boolean aShouldScroll) {

		List<ArtistDto> artists = getView().getArtists();

		if (artists != null && artists.size() > 0) {

			ArtistDto artistToSelect = findArtist(aArtist);

			if (artistToSelect != null) {
				getView().setSelectedArtist(artistToSelect, aShouldScroll);
			} else if (aArtist != null && !aArtist.trim().equals("")) {
				log.warning("could not find artist [" + aArtist + "]");
			}

			if (getView().getSelectedArtist() == null) {
				getView().setSelectedArtist(artists.get(0), aShouldScroll);
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
