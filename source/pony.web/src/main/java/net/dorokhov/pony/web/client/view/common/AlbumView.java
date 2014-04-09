package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.shared.AlbumSongsDto;

public class AlbumView extends Composite {

	interface AlbumViewUiBinder extends UiBinder<Widget, AlbumView> {}

	private static AlbumViewUiBinder uiBinder = GWT.create(AlbumViewUiBinder.class);

	@UiField
	Label albumNameLabel;

	@UiField
	Label albumYearLabel;

	private AlbumSongsDto album;

	public AlbumView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public AlbumSongsDto getAlbum() {
		return album;
	}

	public void setAlbum(AlbumSongsDto aAlbum) {

		album = aAlbum;

		updateAlbum();
	}

	private void updateAlbum() {
		albumNameLabel.setText(album != null ? album.getName() : null);
		albumYearLabel.setText(album != null ? StringUtils.nullSafeToString(album.getYear()) : null);
	}
}
