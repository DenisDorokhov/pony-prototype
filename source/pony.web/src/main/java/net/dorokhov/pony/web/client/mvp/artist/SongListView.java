package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SongListView  extends Composite {

    interface SongListUiBinder extends UiBinder<Widget, SongListView> {}

    private static SongListUiBinder uiBinder = GWT.create(SongListUiBinder.class);

    private EventBus eventBus;

    @UiField
    Label songListCaption;

    @UiField
    FlowPanel songListPanel;

    private int minSongsInColumn = 5;

    private String caption;

    private ArrayList<SongDto> songs;

    public SongListView() {
        this(null, null);
    }

    public SongListView(ArrayList<SongDto> aSongs) {
        this(aSongs, null);
    }

    public SongListView(ArrayList<SongDto> aSongs, String aCaption) {
        Resources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));

        setCaption(aCaption);
        setSongList(aSongs);
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus aEventBus) {
        eventBus = aEventBus;

        //updateChildrenEventBus();
    }

    public ArrayList<SongDto> getSongList() {
        return songs;
    }

    public void setSongList(ArrayList<SongDto> aSongs) {
        songs = aSongs;

        updateSongList();
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String aCaption) {
        caption = aCaption;

        updateCaption();
    }

    private void updateCaption() {
        songListCaption.setText(caption);
    }

    private void updateSongList() {
        songListPanel.clear();

        ArrayList<List<SongDto>> columns = splitIntoColumns(songs);

        for (List<SongDto> column : columns)
        {
            if (column != null) {
                FlowPanel songsColumnPanel = new FlowPanel();
                songsColumnPanel.setStyleName(Resources.INSTANCE.style().songListColumn());

                for (SongDto song : column) {
                    SongListItem songItem = new SongListItem(song);

                    //songItem.setEventBus(getEventBus());
                    songsColumnPanel.add(songItem);
                }

                songListPanel.add(songsColumnPanel);
            }
        }
    }

    private void updateChildrenEventBus() {
        Iterator<Widget> childWidgets = songListPanel.iterator();

        while (childWidgets.hasNext()) {
            FlowPanel songColumn = (FlowPanel)childWidgets.next();
            Iterator<Widget> columnChildWidgets = songColumn.iterator();

            while (columnChildWidgets.hasNext()) {
                Widget widget = columnChildWidgets.next();

                if (widget instanceof SongListItem) {
                    ((SongListItem)widget).setEventBus(getEventBus());
                }
            }
        }
    }

    private ArrayList<List<SongDto>> splitIntoColumns(ArrayList<SongDto> aList) {
        ArrayList<List<SongDto>> result = new ArrayList<List<SongDto>>();

        if (aList == null || aList.size() <= minSongsInColumn) {
            result.add(aList);
        }
        else {
            int columnSize = (aList.size() / 2) + 1;

            result.add(aList.subList(0, columnSize));
            result.add(aList.subList(columnSize, aList.size()));
        }

        return result;
    }
}
