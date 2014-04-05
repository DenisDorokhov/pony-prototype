package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumSongsDto extends AlbumDto implements Serializable {

	private ArrayList<SongDto> songs;

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
