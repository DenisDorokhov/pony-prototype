package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlbumView extends Composite implements SongDelegate {

	interface MyUiBinder extends UiBinder<Widget, AlbumView> {}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	Image albumImage;

	@UiField
	Label albumNameLabel;

	@UiField
	Label albumYearLabel;

	@UiField
	FlowPanel songListPanel;

	private AlbumSongsDto album;

	private SongDelegate delegate;

	public AlbumView() {

		Resources.INSTANCE.style().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public SongDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SongDelegate aDelegate) {
		delegate = aDelegate;
	}

	public AlbumSongsDto getAlbum() {
		return album;
	}

	public void setAlbum(AlbumSongsDto aAlbum) {

		album = aAlbum;

		updateAlbum();
	}

	@Override
	public void onSongSelected(SongDto aSong) {
		if (getDelegate() != null) {
			getDelegate().onSongSelected(aSong);
		}
	}

	@Override
	public void onSongPlaybackRequested(SongDto aSong) {
		if (getDelegate() != null) {
			getDelegate().onSongPlaybackRequested(aSong);
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

		Map<Integer, ArrayList<SongDto>> albumDiscs = splitIntoDiscs(album != null ? album.getSongs() : new ArrayList<SongDto>());

		for (Map.Entry<Integer, ArrayList<SongDto>> albumDiscEntry : albumDiscs.entrySet()) {

			Integer discNumber = albumDiscEntry.getKey();

			ArrayList<SongDto> songList = albumDiscEntry.getValue();

			SongListView songListView = new SongListView(songList, discNumber != null ? "Disc " + discNumber : null);

			songListView.setDelegate(this);

			songListPanel.add(songListView);
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
