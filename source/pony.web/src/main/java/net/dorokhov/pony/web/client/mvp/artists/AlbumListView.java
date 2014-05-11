package net.dorokhov.pony.web.client.mvp.artists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.view.AlbumView;
import net.dorokhov.pony.web.client.view.event.SongRequestEvent;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;

public class AlbumListView extends ViewWithUiHandlers<AlbumListUiHandlers> implements AlbumListPresenter.MyView, SongRequestEvent.Handler {

	interface MyUiBinder extends UiBinder<Widget, AlbumListView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final List<AlbumView> viewCache = new ArrayList<AlbumView>();

	static {
		for (int i = 0; i < 30; i++) {
			viewCache.add(new AlbumView());
		}
	}

	private final List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

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
	public ContentState getContentState() {
		return contentState;
	}

	@Override
	public void setContentState(ContentState aContentState) {

		contentState = aContentState;

		updateContentState();
	}

	@Override
	public void onSongRequest(SongRequestEvent aEvent) {
		if (aEvent.getAssociatedType() == SongRequestEvent.SONG_SELECTION_REQUESTED) {

			selectionModel.setSelected(aEvent.getSong(), true);

		} else if (aEvent.getAssociatedType() == SongRequestEvent.SONG_ACTIVATION_REQUESTED) {

			if (!aEvent.getSong().equals(activationModel.getSelectedObject())) {
				activationModel.setSelected(aEvent.getSong(), true);
			} else {
				getUiHandlers().onSongActivation(aEvent.getSong());
			}
		}
	}

	private void updateArtist(ArtistDto aOldArtist) {

		artistNameLabel.setText(artist != null ? artist.getName() : null);

		if (artist != aOldArtist) {
			scroller.scrollToTop();
		}
	}

	private void updateAlbums() {

		while (albumsPanel.getWidgetCount() > 0) {

			Widget widget = albumsPanel.getWidget(0);

			albumsPanel.remove(0);

			if (widget instanceof AlbumView) {

				AlbumView albumView = (AlbumView) widget;

				albumView.setSelectionModel(null);
				albumView.setActivationModel(null);
				albumView.setPlaying(false);

				albumView.setAlbum(null);

				viewCache.add(albumView);
			}
		}

		for (HandlerRegistration registration : handlerRegistrations) {
			registration.removeHandler();
		}

		handlerRegistrations.clear();

		if (albums != null) {

			for (AlbumSongsDto album : albums) {

				AlbumView albumView = viewCache.size() > 0 ? viewCache.remove(0) : null;

				if (albumView == null) {
					albumView = new AlbumView();
				}

				albumView.setAlbum(album);

				albumView.setSelectionModel(selectionModel);
				albumView.setActivationModel(activationModel);
				albumView.setPlaying(isPlaying());

				handlerRegistrations.add(albumView.addSongSelectionRequestHandler(this));
				handlerRegistrations.add(albumView.addSongActivationRequestHandler(this));

				albumsPanel.add(albumView);
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