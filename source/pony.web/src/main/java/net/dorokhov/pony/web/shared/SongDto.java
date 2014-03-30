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

	private String artist;

	private String albumArtist;

	private String album;

	private String name;

	private Integer year;

	private Integer artwork;

	private Integer artistId;

	private String artistName;

	private Integer artistArtwork;

	private Integer albumId;

	private String albumName;

	private Integer albumYear;

	private Integer albumArtwork;

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

	public String getArtist() {
		return artist;
	}

	public void setArtist(String aArtist) {
		artist = aArtist;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String aAlbumArtist) {
		albumArtist = aAlbumArtist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String aAlbum) {
		album = aAlbum;
	}

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

	public Integer getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Integer aAlbumId) {
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

	public Integer getAlbumArtwork() {
		return albumArtwork;
	}

	public void setAlbumArtwork(Integer aAlbumArtwork) {
		albumArtwork = aAlbumArtwork;
	}
}
