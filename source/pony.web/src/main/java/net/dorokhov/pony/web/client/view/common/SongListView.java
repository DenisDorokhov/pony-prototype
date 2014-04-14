package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;

public class SongListView  extends Composite {

    interface SongListUiBinder extends UiBinder<Widget, SongListView> {}

    private static SongListUiBinder uiBinder = GWT.create(SongListUiBinder.class);

    @UiField
    Label songListCaption;

    @UiField
    FlowPanel songListPanel;

    private int minSongsInColumn = 5;

    private String caption;

    private ArrayList<SongDto> songs;

    public SongListView() {
        initWidget(uiBinder.createAndBindUi(this));
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
                songsColumnPanel.setStyleName("column");

                for (SongDto song : column) {
                    SongListItem songItem = new SongListItem(song);

                    songsColumnPanel.add(songItem);

                    songItem.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            Window.alert("click!");
                        }
                    });
                }

                songListPanel.add(songsColumnPanel);
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
