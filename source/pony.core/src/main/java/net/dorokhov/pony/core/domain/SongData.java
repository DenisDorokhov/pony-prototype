package net.dorokhov.pony.core.domain;

/**
 * Song file data.
 *
 * This class is used as a container for parsed audio file data.
 */
public class SongData {

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

	private String albumArtist;

	private String album;

	private Integer year;

	private String genre;

	private Artwork artwork;

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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer aYear) {
		year = aYear;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String aGenre) {
		genre = aGenre;
	}

	public Artwork getArtwork() {
		return artwork;
	}

	public void setArtwork(Artwork aArtwork) {
		artwork = aArtwork;
	}

	public static class Artwork {

		private byte[] binaryData;

		private String mimeType;

		private String checksum;

		public Artwork(byte[] aBinaryData, String aChecksum, String aMimeType) {
			binaryData = aBinaryData;
			checksum = aChecksum;
			mimeType = aMimeType;
		}

		public byte[] getBinaryData() {
			return binaryData;
		}

		public String getMimeType() {
			return mimeType;
		}

		public String getChecksum() {
			return checksum;
		}

		@Override
		public String toString() {
			return "Artwork{" +
					"mimeType='" + mimeType + '\'' +
					", checksum='" + checksum + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "SongData{" +
				"path='" + path + '\'' +
				", format='" + format + '\'' +
				", mimeType='" + mimeType + '\'' +
				", size=" + size +
				", duration=" + duration +
				", bitRate=" + bitRate +
				", discNumber=" + discNumber +
				", discCount=" + discCount +
				", trackNumber=" + trackNumber +
				", trackCount=" + trackCount +
				", name='" + name + '\'' +
				", artist='" + artist + '\'' +
				", albumArtist='" + albumArtist + '\'' +
				", album='" + album + '\'' +
				", year=" + year +
				", genre='" + genre + '\'' +
				", artwork=" + artwork +
				'}';
	}
}
