package net.dorokhov.pony.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ArtistsPlace extends Place {

	public final static String PREFIX = "";

	@Prefix(PREFIX)
	public static class Tokenizer implements PlaceTokenizer<ArtistsPlace> {

		@Override
		public ArtistsPlace getPlace(String aToken) {
			return new ArtistsPlace();
		}

		@Override
		public String getToken(ArtistsPlace aPlace) {
			return PREFIX;
		}
	}

}
