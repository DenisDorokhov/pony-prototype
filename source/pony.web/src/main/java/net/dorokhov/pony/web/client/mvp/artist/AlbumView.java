package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;

public class AlbumView extends Composite {

	interface MyUiBinder extends UiBinder<Widget, AlbumView> {}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	public interface Delegate {

		public void onSongSelection(SongDto aSong);

		public void onSongPlaybackRequest(SongDto aSong);

	}

	interface AlbumViewStyle extends CssResource {
		String albumImage();
		String nameContainer();
		String songContainer();
		String trackColumn();
		String durationColumn();
	}

	@UiField
	AlbumViewStyle style;

	@UiField
	Image albumImage;

	@UiField
	Label albumNameLabel;

	@UiField
	Label albumYearLabel;

	@UiField
	CellTable<SongDto> songTable;

	private AlbumSongsDto album;

	private Delegate delegate;

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

	public Delegate getDelegate() {
		return delegate;
	}

	public void setDelegate(Delegate aDelegate) {
		delegate = aDelegate;
	}

	private void initSongTable() {

		TextColumn<SongDto> trackColumn = new TextColumn<SongDto>() {
			@Override
			public String getValue(SongDto aSong) {
				return ObjectUtils.nullSafeToString(aSong.getTrackNumber());
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
				return aSong.getDuration() != null ? StringUtils.secondsToMinutes(aSong.getDuration()) : null;
			}
		};

		trackColumn.setCellStyleNames(style.trackColumn());
		durationColumn.setCellStyleNames(style.durationColumn());

		songTable.addColumn(trackColumn);
		songTable.addColumn(nameColumn);
		songTable.addColumn(durationColumn);

		final SingleSelectionModel<SongDto> selectionModel = new SingleSelectionModel<SongDto>();

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (getDelegate() != null) {
					getDelegate().onSongSelection(selectionModel.getSelectedObject());
				}
			}
		});

		songTable.setSelectionModel(selectionModel);
		songTable.addDomHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {

				SongDto song = selectionModel.getSelectedObject();

				if (song != null && getDelegate() != null) {
					getDelegate().onSongPlaybackRequest(song);
				}
			}
		}, DoubleClickEvent.getType());
	}

	private void updateAlbum() {

		String imageUrl = album != null ? album.getArtworkUrl() : null;
		if (imageUrl == null) {
			imageUrl = GWT.getHostPageBaseURL() + "img/unknown.png";
		}

		albumImage.setUrl(imageUrl);

		albumNameLabel.setText(album != null ? album.getName() : null);
		albumYearLabel.setText(album != null ? ObjectUtils.nullSafeToString(album.getYear()) : null);

		songTable.setRowData(album != null ? album.getSongs() : new ArrayList<SongDto>());
	}

}
