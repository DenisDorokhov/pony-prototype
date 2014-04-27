package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiRenderer;
import net.dorokhov.pony.web.shared.ArtistDto;

public class ArtistCell extends AbstractCell<ArtistDto> {

	interface MyUiBinder extends UiRenderer {
		void render(SafeHtmlBuilder sb, ArtistDto artist, SafeUri artworkUrl);
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@Override
	public void render(Context aContext, ArtistDto aArtist, SafeHtmlBuilder aSafeHtmlBuilder) {
		if (aArtist != null) {

			String artworkUrl = aArtist.getArtworkUrl();
			if (artworkUrl == null) {
				artworkUrl = GWT.getHostPageBaseURL() + "img/unknown.png";
			}

			uiBinder.render(aSafeHtmlBuilder, aArtist, UriUtils.fromString(artworkUrl));
		}
	}
}
