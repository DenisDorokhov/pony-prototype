package net.dorokhov.pony.core.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "album", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "artist_id"}))
public class Album extends AbstractEntityIdentified {

	private String name;

	private Integer year;

	private Integer discCount;

	private Integer trackCount;

	private List<Song> songs;

	private Artist artist;

	@Column(name = "name")
	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	@Column(name = "year")
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer aYear) {
		year = aYear;
	}

	@Column(name = "disc_count")
	public Integer getDiscCount() {
		return discCount;
	}

	public void setDiscCount(Integer aDiscCount) {
		discCount = aDiscCount;
	}

	@Column(name = "track_count")
	public Integer getTrackCount() {
		return trackCount;
	}

	public void setTrackCount(Integer aTrackCount) {
		trackCount = aTrackCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "album")
	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> aSongs) {
		songs = aSongs;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "artist_id")
	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist aArtist) {
		artist = aArtist;
	}

	@Override
	public String toString() {
		return "Album{" +
				"id=" + getId() +
				", artist='" + artist + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
