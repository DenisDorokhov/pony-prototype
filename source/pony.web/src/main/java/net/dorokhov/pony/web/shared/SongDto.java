package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class SongDto extends AbstractEntityDto implements Serializable {

	private Long file;

	private String fileUrl;

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

	private Long artwork;

	private String artworkUrl;

	private Long artistId;

	private String artistName;

	private Long artistArtwork;

	private String artistArtworkUrl;

	private Long albumId;

	private String albumName;

	private Integer albumYear;

	private Long albumArtwork;

	private String albumArtworkUrl;

	public Long getFile() {
		return file;
	}

	public void setFile(Long aFile) {
		file = aFile;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String aFileUrl) {
		fileUrl = aFileUrl;
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

	public Long getArtistArtwork() {
		return artistArtwork;
	}

	public void setArtistArtwork(Long aArtistArtwork) {
		artistArtwork = aArtistArtwork;
	}

	public String getArtistArtworkUrl() {
		return artistArtworkUrl;
	}

	public void setArtistArtworkUrl(String aArtistArtworkUrl) {
		artistArtworkUrl = aArtistArtworkUrl;
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

	public Long getAlbumArtwork() {
		return albumArtwork;
	}

	public void setAlbumArtwork(Long aAlbumArtwork) {
		albumArtwork = aAlbumArtwork;
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
				", artist='" + artist + '\'' +
				", album='" + album + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
