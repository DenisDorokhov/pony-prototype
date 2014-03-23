package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class SongDto extends AbstractEntityDto implements Serializable {

	private Integer file;

	private String path;

	private String format;

	private String mimeType;

	private Long size;

	private Integer duration;

	private Long bitRate;

	private Integer discNumber;

	private Integer discCount;

	private Integer trackNumber;

	private Integer trackCount;

	private String name;

	private String artist;

	private Integer artistArtwork;

	private String album;

	private Integer albumArtwork;

	private Integer year;

	private Integer artwork;

	private Integer albumId;

	private Integer artistId;

	public Integer getFile() {
		return file;
	}

	public void setFile(Integer aFile) {
		file = aFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String aPath) {
		path = aPath;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String aFormat) {
		format = aFormat;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String aMimeType) {
		mimeType = aMimeType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long aSize) {
		size = aSize;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer aDuration) {
		duration = aDuration;
	}

	public Long getBitRate() {
		return bitRate;
	}

	public void setBitRate(Long aBitRate) {
		bitRate = aBitRate;
	}

	public Integer getDiscNumber() {
		return discNumber;
	}

	public void setDiscNumber(Integer aDiscNumber) {
		discNumber = aDiscNumber;
	}

	public Integer getDiscCount() {
		return discCount;
	}

	public void setDiscCount(Integer aDiscCount) {
		discCount = aDiscCount;
	}

	public Integer getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(Integer aTrackNumber) {
		trackNumber = aTrackNumber;
	}

	public Integer getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(Integer aTrackCount) {
		trackCount = aTrackCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
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

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String aAlbum) {
		album = aAlbum;
	}

	public Integer getAlbumArtwork() {
		return albumArtwork;
	}

	public void setAlbumArtwork(Integer aAlbumArtwork) {
		albumArtwork = aAlbumArtwork;
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

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer aAlbumId) {
		albumId = aAlbumId;
	}

	public Integer getArtistId() {
		return artistId;
	}

	public void setArtistId(Integer aArtistId) {
		artistId = aArtistId;
	}
}
