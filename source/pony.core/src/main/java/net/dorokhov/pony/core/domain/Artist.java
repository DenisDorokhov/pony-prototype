package net.dorokhov.pony.core.domain;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Artist entity.
 */
@Entity
@Table(name = "artist")
@Indexed
public class Artist extends AbstractEntity<Integer> {

    private String name;

	private StoredFile artwork;

    private List<Album> albums;

	@Column(name = "name", unique = true)
	@NotBlank
	@Field
	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "artwork_stored_file_id")
	public StoredFile getArtwork() {
		return artwork;
	}

	public void setArtwork(StoredFile aArtwork) {
		artwork = aArtwork;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "artist")
	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> aAlbums) {
		albums = aAlbums;
	}

	@Override
	public String toString() {
		return "Artist{" +
				"id=" + getId() +
				", name='" + name + '\'' +
				'}';
	}
}
