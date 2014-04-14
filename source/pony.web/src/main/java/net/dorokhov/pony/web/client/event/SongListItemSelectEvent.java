package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.client.view.common.SongListItem;

public class SongListItemSelectEvent extends AbstractEvent<SongListItemSelectEvent.Handler> {

    public static interface Handler extends EventHandler {
        public void onSongItemSelect(SongListItemSelectEvent aEvent);
    }

    public static final Type<Handler> SONG_ITEM_SELECTED = new Type<Handler>();

    private SongListItem songListItem;

    public SongListItemSelectEvent(Type<Handler> aAssociatedType, SongListItem aSongListItem) {

        super(aAssociatedType);

        songListItem = aSongListItem;
    }

    public SongListItem getSongListItem() {
        return songListItem;
    }

    @Override
    protected void dispatch(Handler aHandler) {
        aHandler.onSongItemSelect(this);
    }
}
