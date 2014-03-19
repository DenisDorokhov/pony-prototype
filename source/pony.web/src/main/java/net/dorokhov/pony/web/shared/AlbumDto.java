package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlbumDto extends AbstractEntityDto implements Serializable {

	private String name;

	private Integer year;

	private Integer discCount;

	private Integer trackCount;

	private Integer artwork;

	private String artist;

	private List<SongDto> songs;

	private Integer artistId;

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer aYear) {
		year = aYear;
	}

	public Integer getDiscCount() {
		return discCount;
	}

	public void setDiscCount(Integer aDiscCount) {
		discCount = aDiscCount;
	}

	public Integer getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(Integer aTrackCount) {
		trackCount = aTrackCount;
	}

	public Integer getArtwork() {
		return artwork;
	}

	public void setArtwork(Integer aArtwork) {
		artwork = aArtwork;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String aArtist) {
		artist = aArtist;
	}

	public List<SongDto> getSongs() {

		if (songs == null) {
			songs = new ArrayList<SongDto>();
		}

		return songs;
	}

	public void setSongs(List<SongDto> aSongs) {
		songs = aSongs;
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(Integer aArtistId) {
		artistId = aArtistId;
	}
}
