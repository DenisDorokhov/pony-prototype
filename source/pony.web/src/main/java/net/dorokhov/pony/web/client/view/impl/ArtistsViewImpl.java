package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.presenter.ArtistsPresenter;
import net.dorokhov.pony.web.client.view.ArtistsView;
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
	SplitLayoutPanel rootContainer;

	@UiField(provided = true)
	CellList<ArtistDto> artistList;

	private List<ArtistDto> artists;

	public ArtistsViewImpl() {

		artistList = new CellList<ArtistDto>(new ArtistCell());

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(ArtistsPresenter aPresenter) {
		presenter = aPresenter;
	}

	@Override
	public ContentState getArtistsContentState() {
		return null; // TODO: implement
	}

	@Override
	public void setArtistsContentState(ContentState aContentState) {
		// TODO: implement
	}

	@Override
	public ContentState getAlbumsContentState() {
		return null;
	}

	@Override
	public void setAlbumsContentState(ContentState aContentState) {
		// TODO: implement
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
		return null;
	}

	@Override
	public void setSelectedArtist(ArtistDto aArtist) {
		// TODO: implement
	}

	@Override
	public List<AlbumSongsDto> getAlbums() {
		return null; // TODO: implement
	}

	@Override
	public void setAlbums(List<AlbumSongsDto> aAlbums) {
		// TODO: implement
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
		artistList.setRowData(artists);
	}
}
