package net.dorokhov.pony.web.domain.response;

import net.dorokhov.pony.web.domain.AlbumDto;
import net.dorokhov.pony.web.domain.ArtistDto;
import net.dorokhov.pony.web.domain.SongDto;

public class SongResponse extends AbstractResponse {

	private SongDto song;

	private AlbumDto album;

	private ArtistDto artist;

	public SongDto getSong() {
		return song;
	}

	public void setSong(SongDto aSong) {
		song = aSong;
	}

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
