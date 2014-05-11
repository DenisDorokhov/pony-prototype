package net.dorokhov.pony.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface Resources  extends ClientBundle {

	public static final Resources IMPL = GWT.create(Resources.class);

	@Source("img/unknown.png")
	ImageResource imgUnknown();

	@Source("img/speaker.png")
	ImageResource imgSpeaker();

	@Source("img/pause.png")
	ImageResource imgPause();

	@Source("css/global.css")
	CssResource cssGlobal();

	@Source("css/artistList.css")
	ArtistListCssResource cssArtistList();

	@Source("css/albumList.css")
	AlbumListCssResource cssAlbumList();

	public interface ArtistListCssResource extends CssResource {

		String artistView();
		String artistView_selected();

		String artistName();
		String artistImage();
	}

	public interface AlbumListCssResource extends CssResource {

		String albumArtistName();

		String album();
		String albumImage();
		String albumInfo();
		String albumHeader();
		String albumName();
		String albumYear();
		String albumDisc();
		String albumDiscHeader();

		String songList();

		String songView();
		String songView_selected();
		String songView_activated();
		String songView_paused();

		String songTrackNumber();
		String songName();
		String songDuration();
	}
}
