package net.dorokhov.pony.core.entity;

import net.dorokhov.pony.core.entity.common.BaseEntityIdentified;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "song_file")
public class SongFile extends BaseEntityIdentified {

	private String path;

	private String type;

	private Long size;

	private Integer duration;

	private Long bitRate;

	private Integer discNumber;

	private Integer discCount;

	private Integer trackNumber;

	private Integer trackCount;

	private String name;

	private String artist;

	private String album;

	private int year;

	@Column(name = "path", unique = true)
	@NotBlank
	public String getPath() {
		return path;
	}

	public void setPath(String aPath) {
		path = aPath;
	}

	@Column(name = "type")
	@NotBlank
	public String getType() {
		return type;
	}

	public void setType(String aType) {
		type = aType;
	}

	@Column(name = "size")
	@NotNull
	public Long getSize() {
		return size;
	}

	public void setSize(Long aSize) {
		size = aSize;
	}

	@Column(name = "duration")
	@NotNull
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer aDuration) {
		duration = aDuration;
	}

	@Column(name = "bit_rate")
	@NotNull
	public Long getBitRate() {
		return bitRate;
	}

	public void setBitRate(Long aBitRate) {
		bitRate = aBitRate;
	}

	@Column(name = "disc_number")
	public Integer getDiscNumber() {
		return discNumber;
	}

	public void setDiscNumber(Integer aDiscNumber) {
		discNumber = aDiscNumber;
	}

	@Column(name = "disc_count")
	public Integer getDiscCount() {
		return discCount;
	}

	public void setDiscCount(Integer aDiscCount) {
		discCount = aDiscCount;
	}

	@Column(name = "track_number")
	public Integer getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(Integer aTrackNumber) {
		trackNumber = aTrackNumber;
	}

	@Column(name = "track_count")
	public Integer getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(Integer aTrackCount) {
		trackCount = aTrackCount;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	@Column(name = "artist")
	public String getArtist() {
		return artist;
	}

	public void setArtist(String aArtist) {
		artist = aArtist;
	}

	@Column(name = "album")
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String aAlbum) {
		album = aAlbum;
	}

	@Column(name = "year")
	public int getYear() {
		return year;
	}

	public void setYear(int aYear) {
		year = aYear;
	}

	@Override
	public String toString() {
		return "SongFile{" +
				"path='" + path + '\'' +
				", type='" + type + '\'' +
				", size=" + size +
				", duration=" + duration +
				", bitRate=" + bitRate +
				", discNumber=" + discNumber +
				", discCount=" + discCount +
				", trackNumber=" + trackNumber +
				", trackCount=" + trackCount +
				", name='" + name + '\'' +
				", artist='" + artist + '\'' +
				", album='" + album + '\'' +
				", year=" + year +
				'}';
	}
}
