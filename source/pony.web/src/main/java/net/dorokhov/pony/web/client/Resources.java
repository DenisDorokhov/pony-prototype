package net.dorokhov.pony.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface Resources  extends ClientBundle {

    public static final Resources IMPL = GWT.create(Resources.class);

    @Source("img/speaker.png")
    ImageResource speaker();

    @Source("img/pause.png")
    ImageResource pause();

    @Source("css/global.css")
    CssResource global();

    @Source("css/songlist.css")
    SongListCssResource songlist();

    public interface SongListCssResource extends CssResource {
        String artistName();
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
