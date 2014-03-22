package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.List;

public class SearchDto implements Serializable {

	private List<ArtistDto> artists;

	private List<AlbumDto> albums;

	private List<SongDto> songs;

	public List<ArtistDto> getArtists() {
		return artists;
	}

	public void setArtists(List<ArtistDto> aArtists) {
		artists = aArtists;
	}

	public List<AlbumDto> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumDto> aAlbums) {
		albums = aAlbums;
	}

	public List<SongDto> getSongs() {
		return songs;
	}

	public void setSongs(List<SongDto> aSongs) {
		songs = aSongs;
	}
}
