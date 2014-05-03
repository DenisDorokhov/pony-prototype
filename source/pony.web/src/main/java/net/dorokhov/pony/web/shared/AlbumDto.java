package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class AlbumDto extends AbstractEntityDto<Long> implements Serializable {

	private String name;

	private Integer year;

	private Long artwork;

	private String artworkUrl;

	private Long artistId;

	private String artistName;

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

	public Long getArtwork() {
		return artwork;
	}

	public void setArtwork(Long aArtwork) {
		artwork = aArtwork;
	}

	public String getArtworkUrl() {
		return artworkUrl;
	}

	public void setArtworkUrl(String aArtworkUrl) {
		artworkUrl = aArtworkUrl;
	}

	public Long getArtistId() {
		return artistId;
	}

	public void setArtistId(Long aArtistId) {
		artistId = aArtistId;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String aArtistName) {
		artistName = aArtistName;
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
