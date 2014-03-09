package net.dorokhov.pony.web.domain.response;

import net.dorokhov.pony.web.domain.AlbumDto;
import net.dorokhov.pony.web.domain.ArtistDto;

public class AlbumResponse extends AbstractResponse {

	private AlbumDto album;

	private ArtistDto artist;

	public AlbumDto getAlbum() {
		return album;
	}

	public void setAlbum(AlbumDto aAlbum) {
		album = aAlbum;
	}

	public ArtistDto getArtist() {
		return artist;
	}

	public void setArtist(ArtistDto aArtist) {
		artist = aArtist;
	}
}
