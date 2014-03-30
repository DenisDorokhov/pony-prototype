package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class AlbumDto extends AbstractEntityDto implements Serializable {

	private String name;

	private Integer year;

	private Integer artwork;

	private String artist;

	private Integer artistArtwork;

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

	public Integer getArtistArtwork() {
		return artistArtwork;
	}

	public void setArtistArtwork(Integer aArtistArtwork) {
		artistArtwork = aArtistArtwork;
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(Integer aArtistId) {
		artistId = aArtistId;
	}

}
