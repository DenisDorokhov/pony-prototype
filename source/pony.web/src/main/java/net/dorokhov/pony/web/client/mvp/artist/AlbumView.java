package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlowPanel;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.client.view.common.SongListView;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.Map;

public class AlbumView extends Composite {

    interface MyUiBinder extends UiBinder<Widget, AlbumView> {}

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface AlbumViewStyle extends CssResource {
		String albumImage();
		String nameContainer();
		String songContainer();
		String trackColumn();
		String durationColumn();
	}

    public interface Delegate {

        public void onSongSelection(SongDto aSong);

        public void onSongPlaybackRequest(SongDto aSong);

    }
    
	private EventBus eventBus;

	@UiField
	AlbumViewStyle style;

	@UiField
	Image albumImage;

	@UiField
	Label albumNameLabel;

	@UiField
	Label albumYearLabel;

    @UiField
    FlowPanel songListPanel;

	private AlbumSongsDto album;

	public AlbumView() {

		initWidget(uiBinder.createAndBindUi(this));

		//initSongTable();
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus aEventBus) {
		eventBus = aEventBus;
	}

	public AlbumSongsDto getAlbum() {
		return album;
	}

	public void setAlbum(AlbumSongsDto aAlbum) {

		album = aAlbum;

		updateAlbum();
	}

//	private void initSongTable() {
//
//		TextColumn<SongDto> trackColumn = new TextColumn<SongDto>() {
//			@Override
//			public String getValue(SongDto aSong) {
//				return StringUtils.nullSafeToString(aSong.getTrackNumber());
//			}
//		};
//		TextColumn<SongDto> nameColumn = new TextColumn<SongDto>() {
//			@Override
//			public String getValue(SongDto aSong) {
//				return aSong.getName();
//			}
//		};
//		TextColumn<SongDto> durationColumn = new TextColumn<SongDto>() {
//			@Override
//			public String getValue(SongDto aSong) {
//				return aSong.getDuration() != null ? TimeUtils.secondsToMinutes(aSong.getDuration()) : null;
//			}
//		};
//
//		trackColumn.setCellStyleNames(style.trackColumn());
//		durationColumn.setCellStyleNames(style.durationColumn());
//
//		songTable.addColumn(trackColumn);
//		songTable.addColumn(nameColumn);
//		songTable.addColumn(durationColumn);
//
//		final SingleSelectionModel<SongDto> selectionModel = new SingleSelectionModel<SongDto>();
//
//		songTable.setSelectionModel(selectionModel);
//		songTable.addDomHandler(new DoubleClickHandler() {
//			@Override
//			public void onDoubleClick(DoubleClickEvent event) {
//
//				SongDto song = selectionModel.getSelectedObject();
//
//				if (song != null) {
//					getEventBus().fireEvent(new SongPlaybackEvent(SongPlaybackEvent.PLAYBACK_REQUESTED, song));
//				}
//			}
//		}, DoubleClickEvent.getType());
//	}

    private void updateAlbum() {

        String imageUrl = album != null ? album.getArtworkUrl() : null;
        if (imageUrl == null) {
            imageUrl = GWT.getHostPageBaseURL() + "img/unknown.png";
        }

        albumImage.setUrl(imageUrl);

        albumNameLabel.setText(album != null ? album.getName() : null);
        albumYearLabel.setText(album != null ? StringUtils.nullSafeToString(album.getYear()) : null);

        songListPanel.clear();

        Map<Integer, ArrayList<SongDto>> albumDiscs = splitIntoDiscs(album != null ? album.getSongs() : new ArrayList<SongDto>());

        for (Map.Entry<Integer, ArrayList<SongDto>> albumDiscEntry : albumDiscs.entrySet()) {
            Integer discNumber = albumDiscEntry.getKey();
            ArrayList<SongDto> songList = albumDiscEntry.getValue();

            SongListView songListView =
                    new SongListView(songList, discNumber != null ? "Disc " + discNumber : null);

            songListView.setEventBus(getEventBus());

            songListPanel.add(songListView);
        }
    }

    private Map<Integer, ArrayList<SongDto>> splitIntoDiscs(ArrayList<SongDto> aSongs) {
        Map<Integer, ArrayList<SongDto>> result = new HashMap<Integer, ArrayList<SongDto>>();

        for (SongDto song : aSongs) {
            if (result.get(song.getDiscNumber()) == null) {
                result.put(song.getDiscNumber(), new ArrayList<SongDto>());
            }

            result.get(song.getDiscNumber()).add(song);
        }

        return result;
    }
}
