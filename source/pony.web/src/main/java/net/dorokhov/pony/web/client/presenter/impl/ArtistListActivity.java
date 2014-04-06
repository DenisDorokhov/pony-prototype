package net.dorokhov.pony.web.client.presenter.impl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.place.AlbumListPlace;
import net.dorokhov.pony.web.client.presenter.ArtistListPresenter;
import net.dorokhov.pony.web.client.service.ArtistServiceAsync;
import net.dorokhov.pony.web.client.view.ArtistListView;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.List;

public class ArtistListActivity extends AbstractActivity implements ArtistListPresenter {

	private ArtistServiceAsync artistService;

	private PlaceController placeController;

	private ArtistListView view;

	private List<ArtistDto> artists;

	@Inject
	public void setArtistService(ArtistServiceAsync aArtistService) {
		artistService = aArtistService;
	}

	@Inject
	public void setPlaceController(PlaceController aPlaceController) {
		placeController = aPlaceController;
	}

	@Inject
	public void setView(ArtistListView aView) {
		view = aView;
	}

	@Override
	public void start(final AcceptsOneWidget aPanel, EventBus aEventBus) {

		view.setPresenter(this);
		view.setContentState(ContentState.LOADING);

		artistService.getAll(new AsyncCallback<ArrayList<ArtistDto>>() {

			@Override
			public void onSuccess(ArrayList<ArtistDto> aResult) {

				artists = aResult;

				view.setArtists(artists);
				view.setContentState(ContentState.LOADED);

				aPanel.setWidget(view.asWidget());
			}

			@Override
			public void onFailure(Throwable aCaught) {

				view.setContentState(ContentState.ERROR);

				Window.alert(aCaught.getMessage());
			}
		});
	}

	@Override
	public void onArtistSelected(ArtistDto aArtist) {
		placeController.goTo(new AlbumListPlace(aArtist.getId().toString()));
	}

}
