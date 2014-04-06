package net.dorokhov.pony.web.client.presenter.impl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.presenter.AlbumListPresenter;
import net.dorokhov.pony.web.client.service.AlbumServiceAsync;
import net.dorokhov.pony.web.client.view.AlbumListView;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;

public class AlbumListActivity extends AbstractActivity implements AlbumListPresenter {

	private AlbumServiceAsync albumService;

	private AlbumListView view;

	private String artistIdOrName;

	private List<AlbumSongsDto> albums;

	@Inject
	public void setAlbumService(AlbumServiceAsync aAlbumService) {
		albumService = aAlbumService;
	}

	@Inject
	public void setView(AlbumListView aView) {
		view = aView;
	}

	public void setArtistIdOrName(String aArtistIdOrName) {
		artistIdOrName = aArtistIdOrName;
	}

	@Override
	public void start(final AcceptsOneWidget aPanel, EventBus aEventBus) {

		view.setPresenter(this);
		view.setContentState(ContentState.LOADING);

		albumService.getByArtistIdOrName(artistIdOrName, new AsyncCallback<ArrayList<AlbumSongsDto>>() {

			@Override
			public void onSuccess(ArrayList<AlbumSongsDto> aResult) {

				albums = aResult;

				view.setAlbums(albums);
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
	public void onSongSelected(SongDto aSong) {
		// TODO: implement
	}

	@Override
	public void onSongActivated(SongDto aSong) {
		// TODO: implement
	}

}
