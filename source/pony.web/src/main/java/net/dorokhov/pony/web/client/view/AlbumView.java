package net.dorokhov.pony.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SetSelectionModel;
import net.dorokhov.pony.web.client.LocaleMessages;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.view.event.SongRequestEvent;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.*;

public class AlbumView extends Composite implements SongRequestEvent.HasHandler, SongRequestEvent.Handler {

	interface MyUiBinder extends UiBinder<Widget, AlbumView> {}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final List<SongListView> viewCache = new ArrayList<SongListView>();

	static {
		for (int i = 0; i < 50; i++) {
			viewCache.add(new SongListView());
		}
	}

	private final HandlerManager handlerManager = new HandlerManager(this);

	@UiField
	FlowPanel albumView;

	@UiField
	Image albumImage;

	@UiField
	Label albumNameLabel;

	@UiField
	Label albumYearLabel;

	@UiField
	FlowPanel songsPanel;

	private SetSelectionModel<SongDto> selectionModel;
	private SetSelectionModel<SongDto> activationModel;

	private boolean playing;

	private AlbumSongsDto album;

	public AlbumView() {

		Resources.IMPL.cssAlbumList().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public SetSelectionModel<SongDto> getSelectionModel() {
		return selectionModel;
	}

	public void setSelectionModel(SetSelectionModel<SongDto> aSelectionModel) {

		selectionModel = aSelectionModel;

		for (Widget widget : songsPanel) {
			if (widget instanceof SongListView) {
				((SongListView) widget).setSelectionModel(selectionModel);
			}
		}
	}

	public SetSelectionModel<SongDto> getActivationModel() {
		return activationModel;
	}

	public void setActivationModel(SetSelectionModel<SongDto> aActivationModel) {

		activationModel = aActivationModel;

		for (Widget widget : songsPanel) {
			if (widget instanceof SongListView) {
				((SongListView) widget).setActivationModel(activationModel);
			}
		}
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean aPlaying) {

		playing = aPlaying;

		for (Widget widget : songsPanel) {
			if (widget instanceof SongListView) {
				((SongListView) widget).setPlaying(playing);
			}
		}
	}

	public AlbumSongsDto getAlbum() {
		return album;
	}

	public void setAlbum(AlbumSongsDto aAlbum) {

		album = aAlbum;

		updateAlbum();
	}

	public void scrollToSong(SongDto aSong) {
		for (int i = 0; i < songsPanel.getWidgetCount(); i++) {

			SongListView songListView = (SongListView) songsPanel.getWidget(i);

			songListView.scrollToSong(aSong);
		}
	}

	@Override
	public HandlerRegistration addSongSelectionRequestHandler(SongRequestEvent.Handler aHandler) {
		return handlerManager.addHandler(SongRequestEvent.SONG_SELECTION_REQUESTED, aHandler);
	}

	@Override
	public HandlerRegistration addSongActivationRequestHandler(SongRequestEvent.Handler aHandler) {
		return handlerManager.addHandler(SongRequestEvent.SONG_ACTIVATION_REQUESTED, aHandler);
	}

	@Override
	public void onSongRequest(SongRequestEvent aEvent) {
		if (aEvent.getAssociatedType() == SongRequestEvent.SONG_SELECTION_REQUESTED) {
			handlerManager.fireEvent(new SongRequestEvent(SongRequestEvent.SONG_SELECTION_REQUESTED, aEvent.getSong()));
		} else if (aEvent.getAssociatedType() == SongRequestEvent.SONG_ACTIVATION_REQUESTED) {
			handlerManager.fireEvent(new SongRequestEvent(SongRequestEvent.SONG_ACTIVATION_REQUESTED, aEvent.getSong()));
		}
	}

	private void updateAlbum() {

		Map<Integer, List<SongDto>> albumDiscs = splitIntoDiscs(getAlbum());

		while (songsPanel.getWidgetCount() > albumDiscs.size()) {

			int i = songsPanel.getWidgetCount() - 1;

			SongListView songListView = (SongListView) songsPanel.getWidget(i);

			songsPanel.remove(i);

			songListView.setSelectionModel(null);
			songListView.setActivationModel(null);
			songListView.setPlaying(false);

			songListView.setSongs(null);

			viewCache.add(songListView);
		}

		int i = 0;

		for (Map.Entry<Integer, List<SongDto>> entry : albumDiscs.entrySet()) {

			SongListView songListView;

			if (i < songsPanel.getWidgetCount()) {
				songListView = (SongListView) songsPanel.getWidget(i);
			} else {

				songListView = viewCache.size() > 0 ? viewCache.remove(0) : null;

				if (songListView == null) {
					songListView = new SongListView();
				}

				songListView.setSelectionModel(getSelectionModel());
				songListView.setActivationModel(getActivationModel());
				songListView.setPlaying(isPlaying());

				songListView.addSongSelectionRequestHandler(this);
				songListView.addSongActivationRequestHandler(this);

				songsPanel.add(songListView);
			}

			Integer discNumber = entry.getKey();

			if (discNumber != null && discNumber == 1 && albumDiscs.size() == 1) {
				discNumber = null;
			}

			songListView.setSongs(entry.getValue());

			songListView.setCaption(discNumber != null ? LocaleMessages.IMPL.albumDiscCaption(discNumber) : null);

			i++;
		}

		if (getAlbum() != null && getAlbum().getArtworkUrl() != null) {
			albumImage.setUrl(getAlbum().getArtworkUrl());
		} else {
			albumImage.setResource(Resources.IMPL.imgUnknown());
		}

		albumNameLabel.setText(getAlbum() != null ? getAlbum().getName() : null);
		albumYearLabel.setText(getAlbum() != null ? ObjectUtils.nullSafeToString(getAlbum().getYear()) : null);
	}

	private Map<Integer, List<SongDto>> splitIntoDiscs(AlbumSongsDto aAlbum) {

		ArrayList<SongDto> aSongs = aAlbum != null ? aAlbum.getSongs() : new ArrayList<SongDto>();

		Map<Integer, List<SongDto>> result = new LinkedHashMap<Integer, List<SongDto>>();

		for (SongDto song : aSongs) {

			Integer discNumber = song.getDiscNumber();

			if (discNumber == null) {
				discNumber = 1;
			}

			List<SongDto> discSongs = result.get(discNumber);

			if (discSongs == null) {

				discSongs = new ArrayList<SongDto>();

				result.put(discNumber, discSongs);
			}

			discSongs.add(song);
		}

		return result;
	}
}
