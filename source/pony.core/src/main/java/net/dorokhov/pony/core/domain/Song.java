package net.dorokhov.pony.core.domain;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

@Entity
@Table(name = "song")
@Indexed
public class Song extends AbstractEntity<Integer> {

	private SongFile file;

	private Album album;

	@OneToOne(optional = false)
	@JoinColumn(name = "song_file_id", unique = true)
	@IndexedEmbedded
	public SongFile getFile() {
		return file;
	}

	public void setFile(SongFile aSongFile) {
		file = aSongFile;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "album_id")
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album aAlbum) {
		album = aAlbum;
	}

	@Override
	public String toString() {
		return "Song{" +
				"id=" + getId() +
				", album='" + album + '\'' +
				", name='" + (file != null ? file.getName() : null) + '\'' +
				", path='" + (file != null ? file.getPath() : null) + '\'' +
				'}';
	}
}
