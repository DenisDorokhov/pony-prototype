package net.dorokhov.pony.web.client.mvp.artists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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

import java.util.List;

public class AlbumListView extends ViewWithUiHandlers<AlbumListUiHandlers> implements AlbumListPresenter.MyView, SongRequestEvent.Handler {

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
	FlowPanel albumsPanel;

	private ArtistDto artist;

	private List<AlbumSongsDto> albums;

	private ContentState contentState;

	private SingleSelectionModel<SongDto> selectionModel;
	private SingleSelectionModel<SongDto> activationModel;

	private boolean shouldScrollToTop;

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
	public SongDto getActivatedSong() {
		return activationModel.getSelectedObject();
	}

	@Override
	public void setActivatedSong(SongDto aSong) {
		if (aSong != null) {
			activationModel.setSelected(aSong, true);
		} else {
			if (activationModel.getSelectedObject() != null) {
				activationModel.setSelected(activationModel.getSelectedObject(), false);
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

		shouldScrollToTop = (artist != aOldArtist);
	}

	private void updateAlbums() {

		albumsPanel.clear();

		if (albums != null) {

			for (AlbumSongsDto album : albums) {

				AlbumView albumView = new AlbumView(selectionModel, activationModel, album);

				albumView.addSongSelectionRequestHandler(this);
				albumView.addSongActivationRequestHandler(this);

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