package net.dorokhov.pony.core.domain;

import net.dorokhov.pony.core.service.search.SearchAnalyzer;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Artist entity.
 */
@Entity
@Table(name = "artist")
@Indexed
public class Artist extends BaseEntity<Long> implements Comparable<Artist> {

	private String name;

	private StoredFile artwork;

	private List<Album> albums;

	@Column(name = "name", unique = true)
	@NotBlank
	@Field(analyzer = @Analyzer(impl = SearchAnalyzer.class))
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

		if (albums == null) {
			albums = new ArrayList<Album>();
		}

		return albums;
	}

	public void setAlbums(List<Album> aAlbums) {
		albums = aAlbums;
	}

	@Override
	@SuppressWarnings("NullableProblems")
	public int compareTo(Artist aArtist) {

		int result = 0;

		if (!equals(aArtist)) {

			String regex = "^the\\s+";

			String name1 = getName() != null ? getName().toLowerCase().replaceAll(regex, "") : null;
			String name2 = aArtist.getName() != null ? aArtist.getName().toLowerCase().replaceAll(regex, "") : null;

			result = ObjectUtils.compare(name1, name2);
		}

		return result;
	}

	@Override
	public String toString() {
		return "Artist{" +
				"id=" + getId() +
				", name='" + name + '\'' +
				'}';
	}
}
