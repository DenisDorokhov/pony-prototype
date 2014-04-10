package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.client.common.TimeUtils;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

public class AlbumView extends Composite {

	interface AlbumViewUiBinder extends UiBinder<Widget, AlbumView> {}

	private static AlbumViewUiBinder uiBinder = GWT.create(AlbumViewUiBinder.class);

	@UiField
	Image albumImage;

	@UiField
	Label albumNameLabel;

	@UiField
	Label albumYearLabel;

	@UiField
	CellTable<SongDto> songTable;

	private AlbumSongsDto album;

	public AlbumView() {

		initWidget(uiBinder.createAndBindUi(this));

		initSongTable();
	}

	public AlbumSongsDto getAlbum() {
		return album;
	}

	public void setAlbum(AlbumSongsDto aAlbum) {

		album = aAlbum;

		updateAlbum();
	}

	private void updateAlbum() {

		String imageUrl = album != null ? album.getArtworkUrl() : null;
		if (imageUrl == null) {
			imageUrl = GWT.getHostPageBaseURL() + "img/unknown.png";
		}

		albumImage.setUrl(imageUrl);

		albumNameLabel.setText(album != null ? album.getName() : null);
		albumYearLabel.setText(album != null ? StringUtils.nullSafeToString(album.getYear()) : null);

		songTable.setRowData(album != null ? album.getSongs() : new ArrayList<SongDto>());
	}

	private void initSongTable() {

		TextColumn<SongDto> trackColumn = new TextColumn<SongDto>() {
			@Override
			public String getValue(SongDto aSong) {
				return StringUtils.nullSafeToString(aSong.getTrackNumber());
			}
		};
		TextColumn<SongDto> nameColumn = new TextColumn<SongDto>() {
			@Override
			public String getValue(SongDto aSong) {
				return aSong.getName();
			}
		};
		TextColumn<SongDto> durationColumn = new TextColumn<SongDto>() {
			@Override
			public String getValue(SongDto aSong) {
				return aSong.getDuration() != null ? TimeUtils.secondsToMinutes(aSong.getDuration()) : null;
			}
		};

		trackColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		durationColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		songTable.addColumn(trackColumn);
		songTable.addColumn(nameColumn);
		songTable.addColumn(durationColumn);

		songTable.setColumnWidth(trackColumn, 50, Style.Unit.PX);
		songTable.setColumnWidth(durationColumn, 80, Style.Unit.PX);
	}
}
