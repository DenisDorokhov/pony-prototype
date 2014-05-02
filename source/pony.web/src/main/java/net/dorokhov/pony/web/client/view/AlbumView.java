package net.dorokhov.pony.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SetSelectionModel;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.view.event.SongRequestEvent;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumView extends Composite implements SongRequestEvent.HasHandler, SongRequestEvent.Handler {

	interface MyUiBinder extends UiBinder<Widget, AlbumView> {}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private final HandlerManager handlerManager = new HandlerManager(this);

	private final ArrayList<SongListView> songListViews = new ArrayList<SongListView>();

	@UiField
	Image albumImage;

	@UiField
	Label albumNameLabel;

	@UiField
	Label albumYearLabel;

	@UiField
	FlowPanel songListPanel;

	private SetSelectionModel<SongDto> selectionModel;
	private SetSelectionModel<SongDto> activationModel;

	private AlbumSongsDto album;

	public AlbumView() {

		Resources.IMPL.style().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public AlbumView(SetSelectionModel<SongDto> aSelectionModel, SetSelectionModel<SongDto> aActivationModel) {

		this();

		setSelectionModel(aSelectionModel);
		setActivationModel(aActivationModel);
	}

	public AlbumView(SetSelectionModel<SongDto> aSelectionModel, SetSelectionModel<SongDto> aActivationModel, AlbumSongsDto aAlbum) {

		this(aSelectionModel, aActivationModel);

		setAlbum(aAlbum);
	}

	public SetSelectionModel<SongDto> getSelectionModel() {
		return selectionModel;
	}

	public void setSelectionModel(SetSelectionModel<SongDto> aSelectionModel) {

		selectionModel = aSelectionModel;

		for (SongListView songListView : songListViews) {
			songListView.setSelectionModel(selectionModel);
		}
	}

	public SetSelectionModel<SongDto> getActivationModel() {
		return activationModel;
	}

	public void setActivationModel(SetSelectionModel<SongDto> aActivationModel) {

		activationModel = aActivationModel;

		for (SongListView songListView : songListViews) {
			songListView.setActivationModel(activationModel);
		}
	}

	public AlbumSongsDto getAlbum() {
		return album;
	}

	public void setAlbum(AlbumSongsDto aAlbum) {

		album = aAlbum;

		updateAlbum();
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

		String imageUrl = album != null ? album.getArtworkUrl() : null;

		if (imageUrl == null) {
			imageUrl = GWT.getHostPageBaseURL() + "img/unknown.png";
		}

		albumImage.setUrl(imageUrl);

		albumNameLabel.setText(album != null ? album.getName() : null);
		albumYearLabel.setText(album != null ? ObjectUtils.nullSafeToString(album.getYear()) : null);

		songListPanel.clear();
		songListViews.clear();

		Map<Integer, List<SongDto>> albumDiscs = splitIntoDiscs(album != null ? album.getSongs() : new ArrayList<SongDto>());

		for (Map.Entry<Integer, List<SongDto>> albumDiscEntry : albumDiscs.entrySet()) {

			Integer discNumber = albumDiscEntry.getKey();

			if (discNumber != null && discNumber == 1 && albumDiscs.size() == 1) {
				discNumber = null;
			}

			List<SongDto> songList = albumDiscEntry.getValue();

			SongListView songListView = new SongListView(getSelectionModel(), getActivationModel(), songList);

			songListView.setCaption(discNumber != null ? "Disc " + discNumber : null);

			songListView.addSongSelectionRequestHandler(this);
			songListView.addSongActivationRequestHandler(this);

			songListPanel.add(songListView);
			songListViews.add(songListView);
		}
	}

	private Map<Integer, List<SongDto>> splitIntoDiscs(ArrayList<SongDto> aSongs) {

		Map<Integer, List<SongDto>> result = new HashMap<Integer, List<SongDto>>();

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
