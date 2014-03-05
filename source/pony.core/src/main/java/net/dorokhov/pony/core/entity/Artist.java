package net.dorokhov.pony.core.entity;

import net.dorokhov.pony.core.entity.common.BaseEntityIdentified;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "artist")
public class Artist extends BaseEntityIdentified {

    private String name;

    private List<Album> albums;

	@Column(name = "name", unique = true)
	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
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
