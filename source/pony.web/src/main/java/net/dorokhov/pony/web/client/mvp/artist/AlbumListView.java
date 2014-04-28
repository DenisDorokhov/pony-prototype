package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;

public class AlbumListView extends ViewWithUiHandlers<AlbumListUiHandlers> implements AlbumListPresenter.MyView, SongDelegate {

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
	ScrollPanel scroller;

	@UiField
	VerticalPanel albumsPanel;

	private List<AlbumView> albumViewCache = new ArrayList<AlbumView>();

	private ArtistDto artist;

	private List<AlbumSongsDto> albums;

	private ContentState contentState;

	private boolean shouldScrollToTop;

	public AlbumListView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public ArtistDto getArtist() {
		return artist;
	}

	@Override
	public void setArtist(ArtistDto aArtist) {

		ArtistDto oldArtist = artist;

		artist = aArtist;

		updateArtist(oldArtist);
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
		getUiHandlers().onSongSelection(aSong);
	}

	@Override
	public void onSongPlaybackRequested(SongDto aSong) {
		getUiHandlers().onSongActivation(aSong);
	}

	private void updateArtist(ArtistDto aOldArtist) {

		artistNameLabel.setText(artist != null ? artist.getName() : null);

		shouldScrollToTop = (artist != aOldArtist);
	}

	private void updateAlbums() {

		while (albumsPanel.getWidgetCount() > 0) {

			Widget widget = albumsPanel.getWidget(0);

			if (widget instanceof AlbumView) {

				AlbumView view = (AlbumView) widget;

				view.setAlbum(null);
				view.setDelegate(null);

				albumViewCache.add(view);
			}

			albumsPanel.remove(0);
		}

		if (albums != null) {

			for (AlbumSongsDto album : albums) {

				AlbumView albumView = albumViewCache.size() > 0 ? albumViewCache.get(0) : null;

				if (albumView != null) {
					albumViewCache.remove(0);
				} else {
					albumView = new AlbumView();
				}

				albumView.setAlbum(album);
				albumView.setDelegate(this);

				albumsPanel.add(albumView);
			}

			if (shouldScrollToTop) {

				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						scroller.scrollToTop();
					}
				});

				shouldScrollToTop = false;
			}
		}
	}

	private void updateContentState() {

		if (getContentState() == null) {

			deck.setVisible(false);

		} else {

			deck.setVisible(true);

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
}