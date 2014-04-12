package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class AlbumDto extends AbstractEntityDto implements Serializable {

	private String name;

	private Integer year;

	private Integer artwork;

	private String artworkUrl;

	private Integer artistId;

	private String artistName;

	private Integer artistArtwork;

	private String artistArtworkUrl;

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

	public String getArtworkUrl() {
		return artworkUrl;
	}

	public void setArtworkUrl(String aArtworkUrl) {
		artworkUrl = aArtworkUrl;
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(Integer aArtistId) {
		artistId = aArtistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String aArtistName) {
		artistName = aArtistName;
	}

	public Integer getArtistArtwork() {
		return artistArtwork;
	}

	public void setArtistArtwork(Integer aArtistArtwork) {
		artistArtwork = aArtistArtwork;
	}

	public String getArtistArtworkUrl() {
		return artistArtworkUrl;
	}

	public void setArtistArtworkUrl(String aArtistArtworkUrl) {
		artistArtworkUrl = aArtistArtworkUrl;
	}

	@Override
	public String toString() {
		return "AlbumDto{" +
				"id=" + getId() +
				", artistName='" + artistName + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
