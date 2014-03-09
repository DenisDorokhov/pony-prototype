package net.dorokhov.pony.web.domain.response;

import net.dorokhov.pony.web.domain.AlbumDto;
import net.dorokhov.pony.web.domain.ArtistDto;

import java.util.List;

public class ArtistResponse extends AbstractResponse {

	private ArtistDto artist;

	private List<AlbumDto> albums;

	public ArtistDto getArtist() {
		return artist;
	}

	public void setArtist(ArtistDto aArtist) {
		artist = aArtist;
	}

	public List<AlbumDto> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumDto> aAlbums) {
		albums = aAlbums;
	}
}
