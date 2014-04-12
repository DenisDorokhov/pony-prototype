package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.List;

public class AlbumListView extends Composite {

	interface AlbumListViewUiBinder extends UiBinder<Widget, AlbumListView> {}

	private static AlbumListViewUiBinder uiBinder = GWT.create(AlbumListViewUiBinder.class);

	private EventBus eventBus;

	@UiField
	Label artistNameLabel;

	@UiField
	VerticalPanel albumList;

	private List<AlbumView> albumViewCache = new ArrayList<AlbumView>();

	private ArtistDto artist;

	private List<AlbumSongsDto> albums;

	public AlbumListView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus aEventBus) {
		eventBus = aEventBus;
	}

	public ArtistDto getArtist() {
		return artist;
	}

	public void setArtist(ArtistDto aArtist) {

		artist = aArtist;

		updateArtist();
	}

	public List<AlbumSongsDto> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumSongsDto> aAlbums) {

		albums = aAlbums;

		updateAlbums();
	}

	private void updateArtist() {
		artistNameLabel.setText(artist != null ? artist.getName() : null);
	}

	private void updateAlbums() {

		while (albumList.getWidgetCount() > 0) {

			Widget widget = albumList.getWidget(0);

			if (widget instanceof AlbumView) {

				AlbumView view = (AlbumView) widget;

				view.setAlbum(null);

				albumViewCache.add(view);
			}

			albumList.remove(0);
		}

		if (albums != null) {
			for (AlbumSongsDto album : albums) {

				AlbumView albumView = albumViewCache.size() > 0 ? albumViewCache.get(0) : null;

				if (albumView != null) {
					albumViewCache.remove(0);
				} else {
					albumView = new AlbumView();
				}

				albumView.setEventBus(getEventBus());
				albumView.setAlbum(album);

				albumList.add(albumView);
			}
		}
	}
}
