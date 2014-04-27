package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.client.event.SongEvent;
import net.dorokhov.pony.web.client.event.SongListItemSelectEvent;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.Objects;


public class SongListItem extends Composite {

    interface SongListItemUiBinder extends UiBinder<Widget, SongListItem> {}

    private static SongListItemUiBinder uiBinder = GWT.create(SongListItemUiBinder.class);

    private EventBus eventBus;

    @UiField
    FocusPanel songItemPanel;

    @UiField
    Label trackNumberLabel;

    @UiField
    Label songNameLabel;

    @UiField
    Label songDurationLabel;

    private SongDto song;

    public SongListItem() {
        this(null);
    }

    public SongListItem(SongDto aSong) {
        Resources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        songItemPanel.sinkEvents(Event.ONCLICK);

        song = aSong;

        updateWidget();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus aEventBus) {
        eventBus = aEventBus;
    }

    public void setSong(SongDto aSong) {
        song = aSong;

        updateWidget();
    }

    public SongDto getSong() {
        return song;
    }

    @UiHandler("songItemPanel")
    void onSongListItemClick(ClickEvent aEvent) {
        SongDto selectedSong = getSong();

        songItemPanel.addStyleName(Resources.INSTANCE.style().songListItem_selected());

        getEventBus().fireEvent(new SongListItemSelectEvent(SongListItemSelectEvent.SONG_ITEM_SELECTED, this));

        if (selectedSong != null) {
            getEventBus().fireEvent(new SongEvent(SongEvent.SONG_SELECTED, selectedSong));
        }
    }

    private void updateWidget() {
        if (song != null) {
            trackNumberLabel.setText(Objects.toString(song.getTrackNumber(), null));
            songNameLabel.setText(song.getName());
            songDurationLabel.setText(StringUtils.secondsToMinutes(song.getDuration()));
        }
        else {
            trackNumberLabel.setText(null);
            songNameLabel.setText(null);
            songDurationLabel.setText(null);
        }
    }
}
