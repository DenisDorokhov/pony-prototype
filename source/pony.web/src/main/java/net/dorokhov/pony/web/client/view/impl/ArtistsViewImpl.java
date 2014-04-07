package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.presenter.ArtistsPresenter;
import net.dorokhov.pony.web.client.view.ArtistsView;
import net.dorokhov.pony.web.client.view.common.AlbumsView;
import net.dorokhov.pony.web.client.view.common.ArtistCell;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.List;

public class ArtistsViewImpl extends Composite implements ArtistsView {

	interface ArtistsViewUiBinder extends UiBinder<Widget, ArtistsViewImpl> {}

	private static ArtistsViewUiBinder uiBinder = GWT.create(ArtistsViewUiBinder.class);

	private ArtistsPresenter presenter;

	@UiField
	DeckLayoutPanel artistsContainer;

	@UiField
	DeckLayoutPanel albumsContainer;

	@UiField
	Widget artistsLoadingLabel;

	@UiField(provided = true)
	CellList<ArtistDto> artistsView;

	@UiField
	Widget albumsLoadingLabel;

	@UiField(provided = true)
	AlbumsView albumsView;

	private SingleSelectionModel<ArtistDto> artistListSelectionModel;

	private ContentState artistsContentState;
	private ContentState albumsContentState;

	private List<ArtistDto> artists;
	private List<AlbumSongsDto> albums;

	public ArtistsViewImpl() {

		artistsView = new CellList<ArtistDto>(new ArtistCell());
		artistListSelectionModel = new SingleSelectionModel<ArtistDto>();

		artistsView.setSelectionModel(artistListSelectionModel);
		artistListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent aEvent) {
				presenter.onArtistSelected(artistListSelectionModel.getSelectedObject());
			}
		});

		albumsView = new AlbumsView();

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(ArtistsPresenter aPresenter) {
		presenter = aPresenter;
	}

	@Override
	public ContentState getArtistsContentState() {
		return artistsContentState;
	}

	@Override
	public void setArtistsContentState(ContentState aContentState) {

		artistsContentState = aContentState;

		updateArtistsContentState();
	}

	@Override
	public ContentState getAlbumsContentState() {
		return albumsContentState;
	}

	@Override
	public void setAlbumsContentState(ContentState aContentState) {

		albumsContentState = aContentState;

		updateAlbumsContentState();
	}

	@Override
	public List<ArtistDto> getArtists() {
		return artists;
	}

	@Override
	public void setArtists(List<ArtistDto> aArtists) {

		artists = aArtists;

		updateArtists();
	}

	@Override
	public ArtistDto getSelectedArtist() {
		return artistListSelectionModel.getSelectedObject();
	}

	@Override
	public void setSelectedArtist(ArtistDto aArtist) {
		artistListSelectionModel.setSelected(aArtist, true);
	}

	@Override
	public List<AlbumSongsDto> getAlbums() {
		return albums;
	}

	@Override
	public void setAlbums(List<AlbumSongsDto> aAlbums) {
		albums = aAlbums;
	}

	@Override
	public SongDto getSelectedSong() {
		return null; // TODO: implement
	}

	@Override
	public void setSelectedSong(SongDto aSong) {
		// TODO: implement
	}

	private void updateArtists() {
		artistsView.setRowData(artists);
	}

	private void updateAlbums() {
		// TODO: implement
	}

	private void updateArtistsContentState() {
		if (getArtistsContentState() == ContentState.LOADED) {
			artistsContainer.showWidget(artistsView);
		} else {
			artistsContainer.showWidget(artistsLoadingLabel);
		}
	}

	private void updateAlbumsContentState() {
		if (getAlbumsContentState() == ContentState.LOADED) {
			albumsContainer.showWidget(albumsView);
		} else {
			albumsContainer.showWidget(albumsLoadingLabel);
		}
	}
}
