package net.dorokhov.pony.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface Resources  extends ClientBundle {

    public static final Resources INSTANCE = GWT.create(Resources.class);

    @Source("style.css")
    Style style();

    public interface Style extends CssResource {
        String album();
        String albumImage();
        String albumInfo();
        String albumHeader();
        String albumName();
        String albumYear();
        String albumDisc();
        String albumDiscHeader();
        String songList();
        String songListColumn();
        String songListItem();
        String songListItem_selected();
        String trackNumber();
        String songName();
        String songDuration();
    }
}
