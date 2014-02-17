package net.dorokhov.pony.core.entity;

import net.dorokhov.pony.core.entity.common.BaseEntityIdentified;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "song")
public class Song extends BaseEntityIdentified {

	private SongFile file;

	private Album album;

	@OneToOne(optional = false)
	public SongFile getFile() {
		return file;
	}

	public void setFile(SongFile aSongFile) {
		file = aSongFile;
	}

	@ManyToOne(optional = true)
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album aAlbum) {
		album = aAlbum;
	}
}
