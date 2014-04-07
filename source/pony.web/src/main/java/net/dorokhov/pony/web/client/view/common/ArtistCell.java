package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import net.dorokhov.pony.web.shared.ArtistDto;

public class ArtistCell extends AbstractCell<ArtistDto> {

	interface ArtistCellUiBinder extends UiRenderer {
		void render(SafeHtmlBuilder sb, ArtistDto artist);
	}

	private static ArtistCellUiBinder uiBinder = GWT.create(ArtistCellUiBinder.class);

	@Override
	public void render(Context aContext, ArtistDto aArtist, SafeHtmlBuilder aSafeHtmlBuilder) {
		if (aArtist != null) {
			uiBinder.render(aSafeHtmlBuilder, aArtist);
		}
	}
}
