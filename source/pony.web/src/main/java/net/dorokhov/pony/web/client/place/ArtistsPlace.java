package net.dorokhov.pony.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ArtistsPlace extends Place {

	private String artistIdOrName;

	public ArtistsPlace() {}

	public ArtistsPlace(String aArtistIdOrName) {
		setArtistIdOrName(aArtistIdOrName);
	}

	public String getArtistIdOrName() {
		return artistIdOrName;
	}

	public void setArtistIdOrName(String aArtistIdOrName) {
		artistIdOrName = aArtistIdOrName;
	}

	@Override
	public int hashCode() {
		return artistIdOrName != null ? artistIdOrName.hashCode() : 0;
	}

	@Override
	public boolean equals(Object aObj) {

		if (this == aObj) {
			return true;
		}

		if (aObj != null && getClass().equals(aObj.getClass())) {

			ArtistsPlace place = (ArtistsPlace) aObj;

			String normalizedValue1 = getArtistIdOrName() != null ? getArtistIdOrName().trim().toLowerCase() : "";
			String normalizedValue2 = place.getArtistIdOrName() != null ? place.getArtistIdOrName().trim().toLowerCase() : "";

			return normalizedValue1.equals(normalizedValue2);
		}

		return false;
	}

	@Override
	public String toString() {
		return "ArtistsPlace{" +
				"artistIdOrName='" + artistIdOrName + '\'' +
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
			return aPlace.getArtistIdOrName();
		}
	}

}
