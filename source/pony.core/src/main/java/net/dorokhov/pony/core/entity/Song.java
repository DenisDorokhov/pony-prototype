package net.dorokhov.pony.core.entity;

import net.dorokhov.pony.core.entity.common.BaseEntityIdentified;

import javax.persistence.*;

@Entity
@Table(name = "song")
public class Song extends BaseEntityIdentified {

	private SongFile file;

	private Album album;

	@OneToOne(optional = false)
	@JoinColumn(name = "song_file_id", unique = true)
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
}
