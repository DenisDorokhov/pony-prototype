package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import net.dorokhov.pony.web.client.common.TimeUtils;
import net.dorokhov.pony.web.shared.SongDto;


public class SongListItem extends Composite implements HasClickHandlers {

    interface SongListItemUiBinder extends UiBinder<Widget, SongListItem> {}

    private static SongListItemUiBinder uiBinder = GWT.create(SongListItemUiBinder.class);

    interface SongListItemStyle extends CssResource {
        String songListItem();
        String trackNumber();
        String songName();
        String songDuration();
    }

    @UiField
    FlowPanel songItemPanel;

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

    public void setSong(SongDto aSong) {
        song = aSong;

        updateWidget();
    }

    public SongDto getSong() {
        return song;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler clickHandler) {
        return songItemPanel.addHandler(clickHandler, ClickEvent.getType());
    }

    private void updateWidget() {
        if (song != null) {
            trackNumberLabel.setText(song.getTrackNumber().toString());
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
