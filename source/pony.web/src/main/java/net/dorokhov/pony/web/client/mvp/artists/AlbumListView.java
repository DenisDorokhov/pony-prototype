package net.dorokhov.pony.web.client.mvp.artists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.view.AlbumView;
import net.dorokhov.pony.web.client.view.event.SongViewEvent;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumListView extends ViewWithUiHandlers<AlbumListUiHandlers> implements AlbumListPresenter.MyView, SongViewEvent.Handler {

	interface MyUiBinder extends UiBinder<Widget, AlbumListView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final List<AlbumView> viewCache = new ArrayList<AlbumView>();

	static {
		for (int i = 0; i < 30; i++) {
			viewCache.add(new AlbumView());
		}
	}

	private final Map<Long, AlbumView> albumIdToAlbumView = new HashMap<Long, AlbumView>();

	@UiField
	DeckLayoutPanel deck;

	@UiField
	Label loadingLabel;

	@UiField
	Label errorLabel;

	@UiField
	Label noDataLabel;

	@UiField
	Widget content;

	@UiField
	Label artistNameLabel;

	@UiField
	ScrollPanel scroller;

	@UiField
	FlowPanel albumsPanel;

	private ArtistDto artist;

	private List<AlbumSongsDto> albums;

	private ContentState contentState;

	private SingleSelectionModel<SongDto> selectionModel;
	private SingleSelectionModel<SongDto> activationModel;

	private boolean playing;

	public AlbumListView() {

		initWidget(uiBinder.createAndBindUi(this));

		selectionModel = new SingleSelectionModel<SongDto>();
		activationModel = new SingleSelectionModel<SongDto>();

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				getUiHandlers().onSongSelection(selectionModel.getSelectedObject());
			}
		});
		activationModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				getUiHandlers().onSongActivation(activationModel.getSelectedObject());
			}
		});
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
	public SongDto getSelectedSong() {
		return selectionModel.getSelectedObject();
	}

	@Override
	public void setSelectedSong(SongDto aSong) {
		if (aSong != null) {
			selectionModel.setSelected(aSong, true);
		} else {
			if (selectionModel.getSelectedObject() != null) {
				selectionModel.setSelected(selectionModel.getSelectedObject(), false);
			}
		}
	}

	@Override
	public SongDto getActiveSong() {
		return activationModel.getSelectedObject();
	}

	@Override
	public void setActiveSong(SongDto aSong) {
		if (aSong != null) {
			activationModel.setSelected(aSong, true);
		} else {
			if (activationModel.getSelectedObject() != null) {
				activationModel.setSelected(activationModel.getSelectedObject(), false);
			}
		}
	}

	@Override
	public boolean isPlaying() {
		return playing;
	}

	@Override
	public void setPlaying(boolean aPlaying) {

		playing = aPlaying;

		for (Widget widget : albumsPanel) {
			if (widget instanceof AlbumView) {
				((AlbumView) widget).setPlaying(playing);
			}
		}
	}

	@Override
	public void scrollToTop() {
		scroller.scrollToTop();
	}

	@Override
	public void scrollToSong(SongDto aSong) {

		AlbumView view = albumIdToAlbumView.get(aSong.getAlbumId());

		if (view != null) {
			view.scrollToSong(aSong);
		}
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
	public void onSongViewEvent(SongViewEvent aEvent) {
		if (aEvent.getAssociatedType() == SongViewEvent.SONG_SELECTION_REQUESTED) {

			selectionModel.setSelected(aEvent.getSong(), true);

		} else if (aEvent.getAssociatedType() == SongViewEvent.SONG_ACTIVATION_REQUESTED) {

			if (!aEvent.getSong().equals(activationModel.getSelectedObject())) {
				activationModel.setSelected(aEvent.getSong(), true);
			} else {
				getUiHandlers().onSongActivation(aEvent.getSong());
			}
		}
	}

	private void updateArtist() {
		artistNameLabel.setText(artist != null ? artist.getName() : null);
	}

	private void updateAlbums() {

		List<AlbumSongsDto> albumList = getAlbums() != null ? getAlbums() : new ArrayList<AlbumSongsDto>();

		while (albumsPanel.getWidgetCount() > albumList.size()) {

			int i = albumsPanel.getWidgetCount() - 1;

			AlbumView albumView = (AlbumView) albumsPanel.getWidget(i);

			albumsPanel.remove(i);

			albumView.setSelectionModel(null);
			albumView.setActivationModel(null);
			albumView.setPlaying(false);

			albumView.setAlbum(null);

			viewCache.add(albumView);
		}

		albumIdToAlbumView.clear();

		for (int i = 0; i < albumList.size(); i++) {

			AlbumSongsDto album = albumList.get(i);

			AlbumView albumView;

			if (i < albumsPanel.getWidgetCount()) {
				albumView = (AlbumView) albumsPanel.getWidget(i);
			} else {

				albumView = viewCache.size() > 0 ? viewCache.remove(0) : null;

				if (albumView == null) {
					albumView = new AlbumView();
				}

				albumView.setSelectionModel(selectionModel);
				albumView.setActivationModel(activationModel);
				albumView.setPlaying(isPlaying());

				albumView.addSongSelectionRequestHandler(this);
				albumView.addSongActivationRequestHandler(this);

				albumsPanel.add(albumView);
			}

			albumView.setAlbum(album);

			albumIdToAlbumView.put(album.getId(), albumView);
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
					if (getArtist() == null || getAlbums() == null || getAlbums().size() == 0) {
						deck.showWidget(noDataLabel);
					} else {
						deck.showWidget(content);
					}
					break;

				default:
					deck.showWidget(errorLabel);
					break;
			}
		}
	}
}