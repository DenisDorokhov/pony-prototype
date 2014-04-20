package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class SongDto extends AbstractEntityDto implements Serializable {

	private String fileUrl;

	private Integer duration;

	private Integer discNumber;

	private Integer trackNumber;

	private String artist;

	private String name;

	private Long artistId;

	private String artistName;

	private Long albumId;

	private String albumName;

	private Integer albumYear;

	private String albumArtworkUrl;

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String aFileUrl) {
		fileUrl = aFileUrl;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer aDuration) {
		duration = aDuration;
	}

	public Integer getDiscNumber() {
		return discNumber;
	}

	public void setDiscNumber(Integer aDiscNumber) {
		discNumber = aDiscNumber;
	}

	public Integer getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(Integer aTrackNumber) {
		trackNumber = aTrackNumber;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String aArtist) {
		artist = aArtist;
	}

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
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

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long aAlbumId) {
		albumId = aAlbumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String aAlbumName) {
		albumName = aAlbumName;
	}

	public Integer getAlbumYear() {
		return albumYear;
	}

	public void setAlbumYear(Integer aAlbumYear) {
		albumYear = aAlbumYear;
	}

	public String getAlbumArtworkUrl() {
		return albumArtworkUrl;
	}

	public void setAlbumArtworkUrl(String aAlbumArtworkUrl) {
		albumArtworkUrl = aAlbumArtworkUrl;
	}

	@Override
	public String toString() {
		return "SongDto{" +
				"id=" + getId() +
				", artist='" + artistName + '\'' +
				", album='" + albumName + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
