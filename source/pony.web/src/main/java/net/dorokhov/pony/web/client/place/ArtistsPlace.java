package net.dorokhov.pony.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import net.dorokhov.pony.web.client.common.StringUtils;

public class ArtistsPlace extends Place {

	private String artist;

	public ArtistsPlace() {}

	public ArtistsPlace(String aArtist) {
		setArtist(aArtist);
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String aArtist) {
		artist = aArtist;
	}

	@Override
	public int hashCode() {
		return artist != null ? artist.hashCode() : 0;
	}

	@Override
	public boolean equals(Object aObj) {

		if (this == aObj) {
			return true;
		}

		if (aObj != null && getClass().equals(aObj.getClass())) {

			ArtistsPlace place = (ArtistsPlace) aObj;

			return StringUtils.nullSafeNormalizedEquals(getArtist(), place.getArtist());
		}

		return false;
	}

	@Override
	public String toString() {
		return "ArtistsPlace{" +
				"artist='" + artist + '\'' +
				'}';
	}

	@Prefix("artists")
	public static class Tokenizer implements PlaceTokenizer<ArtistsPlace> {

		@Override
		public ArtistsPlace getPlace(String aToken) {
			return new ArtistsPlace(aToken);
		}

		@Override
		public String getToken(ArtistsPlace aPlace) {
			return aPlace.getArtist();
		}
	}

}
