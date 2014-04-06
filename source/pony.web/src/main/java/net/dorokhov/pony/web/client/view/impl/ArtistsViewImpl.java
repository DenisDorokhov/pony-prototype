package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.presenter.ArtistsPresenter;
import net.dorokhov.pony.web.client.view.ArtistsView;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.List;

public class ArtistsViewImpl extends Composite implements ArtistsView {

	interface ArtistsViewUiBinder extends UiBinder<HTMLPanel, ArtistsViewImpl> {}

	private static ArtistsViewUiBinder uiBinder = GWT.create(ArtistsViewUiBinder.class);

	private ArtistsPresenter presenter;

	public ArtistsViewImpl() {
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
		return null; // TODO: implement
	}

	@Override
	public void setArtists(List<ArtistDto> aArtists) {
		// TODO: implement
	}

	@Override
	public ArtistDto getSelectedArtist() {
		return null; // TODO: implement
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
}
