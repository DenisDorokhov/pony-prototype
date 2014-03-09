package net.dorokhov.pony.web.domain.response;

import net.dorokhov.pony.web.domain.ArtistDto;

import java.util.ArrayList;
import java.util.List;

public class ArtistListResponse extends AbstractResponse {

	private List<ArtistDto> artists;

	public List<ArtistDto> getArtists() {

		if (artists == null) {
			artists = new ArrayList<ArtistDto>();
		}

		return artists;
	}

	public void setArtists(List<ArtistDto> aArtists) {
		artists = aArtists;
	}
}
