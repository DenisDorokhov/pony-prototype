package net.dorokhov.pony.core.entity;

import net.dorokhov.pony.core.entity.common.BaseEntityIdentified;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "song_file")
public class SongFile extends BaseEntityIdentified {

	private String path;

	private String type;

	private Long size;

	private Long duration;

	private Integer discNumber;

	private Integer trackNumber;

	private String name;

	private String artist;

	private String album;

	private int year;

	@Column(name = "path")
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
	public Long getSize() {
		return size;
	}

	public void setSize(Long aSize) {
		size = aSize;
	}

	@Column(name = "duration")
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long aDuration) {
		duration = aDuration;
	}

	@Column(name = "disc_number")
	public Integer getDiscNumber() {
		return discNumber;
	}

	public void setDiscNumber(Integer aDiscNumber) {
		discNumber = aDiscNumber;
	}

	@Column(name = "track_number")
	public Integer getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(Integer aTrackNumber) {
		trackNumber = aTrackNumber;
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
}
