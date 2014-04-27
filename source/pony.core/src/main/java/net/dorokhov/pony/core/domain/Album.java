package net.dorokhov.pony.core.domain;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Album entity.
 */
@Entity
@Table(name = "album", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "artist_id"}))
@Indexed
public class Album extends AbstractEntity<Long> implements Comparable<Album> {

	private String name;

	private Integer year;

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

	@OneToOne(optional = true, fetch = FetchType.LAZY)
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

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "artist_id")
	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist aArtist) {
		artist = aArtist;
	}

	@Override
	@SuppressWarnings("NullableProblems")
	public int compareTo(Album aAlbum) {

		int result = 0;

		if (!equals(aAlbum)) {

			result = ObjectUtils.compare(getArtist(), aAlbum.getArtist());

			if (result == 0) {
				result = ObjectUtils.compare(getYear(), aAlbum.getYear());
			}
			if (result == 0) {
				result = ObjectUtils.compare(getName(), aAlbum.getName());
			}
		}

		return result;
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
