package net.dorokhov.pony.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ArtistListPlace extends Place {

	public final static String PREFIX = "artists";

	@Prefix(PREFIX)
	public static class Tokenizer implements PlaceTokenizer<ArtistListPlace> {

		@Override
		public ArtistListPlace getPlace(String aToken) {
			return new ArtistListPlace();
		}

		@Override
		public String getToken(ArtistListPlace aPlace) {
			return null;
		}
	}
}
