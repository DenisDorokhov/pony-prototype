package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchDto implements Serializable {

	private ArrayList<ArtistDto> artists;

	private ArrayList<AlbumDto> albums;

	private ArrayList<SongDto> songs;

	public ArrayList<ArtistDto> getArtists() {

		if (artists == null) {
			artists = new ArrayList<ArtistDto>();
		}

		return artists;
	}

	public void setArtists(ArrayList<ArtistDto> aArtists) {
		artists = aArtists;
	}

	public ArrayList<AlbumDto> getAlbums() {

		if (albums == null) {
			albums = new ArrayList<AlbumDto>();
		}

		return albums;
	}

	public void setAlbums(ArrayList<AlbumDto> aAlbums) {
		albums = aAlbums;
	}

	public ArrayList<SongDto> getSongs() {

		if (songs == null) {
			songs = new ArrayList<SongDto>();
		}

		return songs;
	}

	public void setSongs(ArrayList<SongDto> aSongs) {
		songs = aSongs;
	}
}
