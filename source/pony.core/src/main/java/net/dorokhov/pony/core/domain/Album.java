package net.dorokhov.pony.core.domain;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "album", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "artist_id"}))
@Indexed
public class Album extends AbstractEntity<Integer> {

	private String name;

	private Integer year;

	private Integer discCount;

	private Integer trackCount;

	private StoredFile artwork;

	private List<Song> songs;

	private Artist artist;

	@Column(name = "name")
	@NotBlank
	@Field
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

	@OneToOne(optional = true)
	@JoinColumn(name = "artwork_stored_file_id")
	public StoredFile getArtwork() {
		return artwork;
	}

	public void setArtwork(StoredFile aArtwork) {
		artwork = aArtwork;
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
