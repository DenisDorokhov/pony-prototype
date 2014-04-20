package net.dorokhov.pony.core.domain;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

/**
 * Song entity.
 */
@Entity
@Table(name = "song")
@Indexed
public class Song extends AbstractEntity<Long> implements Comparable<Song> {

	private SongFile file;

	private Album album;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "song_file_id", unique = true)
	@IndexedEmbedded
	public SongFile getFile() {
		return file;
	}

	public void setFile(SongFile aSongFile) {
		file = aSongFile;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
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

	@Override
	@SuppressWarnings("NullableProblems")
	public int compareTo(Song aSong) {

		int result = 0;

		if (!equals(aSong)) {

			result = ObjectUtils.compare(getAlbum(), aSong.getAlbum());

			if (result == 0) {

				Integer discNumber1 = getFile() != null && getFile().getDiscNumber() != null ? getFile().getDiscNumber() : 1;
				Integer discNumber2 = aSong.getFile() != null && aSong.getFile().getDiscNumber() != null ? aSong.getFile().getDiscNumber() : 1;

				result = ObjectUtils.compare(discNumber1, discNumber2);
			}
			if (result == 0) {

				Integer trackNumber1 = getFile() != null && getFile().getTrackNumber() != null ? getFile().getTrackNumber() : 1;
				Integer trackNumber2 = aSong.getFile() != null && aSong.getFile().getTrackNumber() != null ? aSong.getFile().getTrackNumber() : 1;

				result = ObjectUtils.compare(trackNumber1, trackNumber2);
			}
			if (result == 0) {

				String name1 = getFile() != null ? getFile().getName() : null;
				String name2 = aSong.getFile() != null ? aSong.getFile().getName() : null;

				result = ObjectUtils.compare(name1, name2);
			}
		}

		return result;
	}
}
