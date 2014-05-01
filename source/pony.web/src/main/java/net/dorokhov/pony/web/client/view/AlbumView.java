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
import net.dorokhov.pony.web.client.view.event.SongActivationEvent;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlbumView extends Composite implements SongActivationEvent.HasHandler, SongActivationEvent.Handler {

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

	private AlbumSongsDto album;

	public AlbumView() {

		Resources.INSTANCE.style().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public AlbumView(SetSelectionModel<SongDto> aSelectionModel) {

		this();

		setSelectionModel(aSelectionModel);
	}

	public AlbumView(SetSelectionModel<SongDto> aSelectionModel, AlbumSongsDto aAlbum) {

		this(aSelectionModel);

		setAlbum(aAlbum);
	}

	public SetSelectionModel<SongDto> getSelectionModel() {
		return selectionModel;
	}

	public void setSelectionModel(SetSelectionModel<SongDto> aSelectionModel) {

		selectionModel = aSelectionModel;

		for (SongListView songListView : songListViews) {
			songListView.setSelectionModel(aSelectionModel);
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
	public void onSongActivation(SongActivationEvent aEvent) {
		handlerManager.fireEvent(new SongActivationEvent(SongActivationEvent.SONG_ACTIVATED, aEvent.getSong()));
	}

	@Override
	public HandlerRegistration addSongActivationHandler(SongActivationEvent.Handler aHandler) {
		return handlerManager.addHandler(SongActivationEvent.SONG_ACTIVATED, aHandler);
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

		Map<Integer, ArrayList<SongDto>> albumDiscs = splitIntoDiscs(album != null ? album.getSongs() : new ArrayList<SongDto>());

		for (Map.Entry<Integer, ArrayList<SongDto>> albumDiscEntry : albumDiscs.entrySet()) {

			Integer discNumber = albumDiscEntry.getKey();

			ArrayList<SongDto> songList = albumDiscEntry.getValue();

			SongListView songListView = new SongListView(getSelectionModel(), songList);

			songListView.setCaption(discNumber != null ? "Disc " + discNumber : null);

			songListView.addSongActivationHandler(this);

			songListPanel.add(songListView);
			songListViews.add(songListView);
		}
	}

	private Map<Integer, ArrayList<SongDto>> splitIntoDiscs(ArrayList<SongDto> aSongs) {

		Map<Integer, ArrayList<SongDto>> result = new HashMap<Integer, ArrayList<SongDto>>();

		for (SongDto song : aSongs) {

			if (result.get(song.getDiscNumber()) == null) {
				result.put(song.getDiscNumber(), new ArrayList<SongDto>());
			}

			result.get(song.getDiscNumber()).add(song);
		}

		return result;
	}
}
