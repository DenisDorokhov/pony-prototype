package net.dorokhov.pony.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class AlbumListPlace extends Place {

	public final static String PREFIX = "albums";

	private String artistIdOrName;

	public AlbumListPlace(String aArtistIdOrName) {
		artistIdOrName = aArtistIdOrName;
	}

	public String getArtistIdOrName() {
		return artistIdOrName;
	}

	@Prefix(PREFIX)
	public static class Tokenizer implements PlaceTokenizer<AlbumListPlace> {

		@Override
		public AlbumListPlace getPlace(String aToken) {
			return new AlbumListPlace(aToken);
		}

		@Override
		public String getToken(AlbumListPlace aPlace) {
			return aPlace.getArtistIdOrName();
		}
	}
}
