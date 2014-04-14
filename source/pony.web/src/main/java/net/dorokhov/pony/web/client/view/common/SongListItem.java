package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.EventBus;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.client.common.TimeUtils;
import net.dorokhov.pony.web.client.event.SongListItemSelectEvent;
import net.dorokhov.pony.web.client.event.SongPlaybackEvent;
import net.dorokhov.pony.web.shared.SongDto;


public class SongListItem extends Composite {

    interface SongListItemUiBinder extends UiBinder<Widget, SongListItem> {}

    private static SongListItemUiBinder uiBinder = GWT.create(SongListItemUiBinder.class);

    interface SongListItemStyle extends CssResource {
        String songListItem();
        String trackNumber();
        String songName();
        String songDuration();
    }

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

        getEventBus().fireEvent(new SongListItemSelectEvent(SongListItemSelectEvent.SONG_ITEM_SELECTED, this));

        if (selectedSong != null) {
            getEventBus().fireEvent(new SongPlaybackEvent(SongPlaybackEvent.PLAYBACK_REQUESTED, selectedSong));
        }
    }

    private void updateWidget() {
        if (song != null) {
            trackNumberLabel.setText(StringUtils.nullSafeToString(song.getTrackNumber()));
            songNameLabel.setText(song.getName());
            songDurationLabel.setText(TimeUtils.secondsToMinutes(song.getDuration()));
        }
        else {
            trackNumberLabel.setText(null);
            songNameLabel.setText(null);
            songDurationLabel.setText(null);
        }
    }
}
