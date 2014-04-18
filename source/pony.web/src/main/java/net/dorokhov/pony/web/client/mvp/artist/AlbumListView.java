package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.List;

public class AlbumListView extends ViewWithUiHandlers<AlbumListUiHandlers> implements AlbumListPresenter.MyView, AlbumView.Delegate {

	interface MyUiBinder extends UiBinder<Widget, AlbumListView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	DeckLayoutPanel deck;

	@UiField
	Label loadingLabel;

	@UiField
	Label errorLabel;

	@UiField
	Widget content;

	@UiField
	Label artistNameLabel;

	@UiField
	VerticalPanel albumsPanel;

	private ArtistDto artist;

	private List<AlbumSongsDto> albums;

	private ContentState contentState;

	public AlbumListView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public ArtistDto getArtist() {
		return artist;
	}

	@Override
	public void setArtist(ArtistDto aArtist) {

		artist = aArtist;

		updateArtist();
	}

	@Override
	public List<AlbumSongsDto> getAlbums() {
		return albums;
	}

	@Override
	public void setAlbums(List<AlbumSongsDto> aAlbums) {

		albums = aAlbums;

		updateAlbums();
	}

	@Override
	public ContentState getContentState() {
		return contentState;
	}

	@Override
	public void setContentState(ContentState aContentState) {

		contentState = aContentState;

		updateContentState();
	}

	@Override
	public void onSongSelected(SongDto aSong) {
		getUiHandlers().onSongSelected(aSong);
	}

	@Override
	public void onSongActivated(SongDto aSong) {
		getUiHandlers().onSongActivated(aSong);
	}

	private void updateArtist() {
		artistNameLabel.setText(artist != null ? artist.getName() : null);
	}

	private void updateAlbums() {

		albumsPanel.clear();

		if (albums != null) {
			for (AlbumSongsDto album : albums) {

				AlbumView albumView = new AlbumView();

				albumView.setAlbum(album);
				albumView.setDelegate(this);

				albumsPanel.add(albumView);
			}
		}
	}

	private void updateContentState() {

		switch (getContentState()) {

			case LOADING:
				deck.showWidget(loadingLabel);
				break;

			case LOADED:
				deck.showWidget(content);
				break;

			default:
				deck.showWidget(errorLabel);
				break;
		}
	}
}
