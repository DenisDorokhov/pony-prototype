package net.dorokhov.pony.core.domain;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;

/**
 * Song entity.
 */
@Entity
@Table(name = "song")
@Indexed
public class Song extends AbstractEntity<Integer> implements Comparable<Song> {

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

	/**
	 * Null safe song comparison. It's useful to resolve inconsistencies like {@literal null} disc number or
	 * {@literal null} track number. {@literal null} for these fields should be taken as {@literal 1}. If song data is
	 * inconsistent, without explicit comparison song order can be incorrect after database query sorting .
	 *
	 * @param aSong song to compare
	 * @return comparison result
	 */
	@SuppressWarnings("NullableProblems")
	@Override
	public int compareTo(Song aSong) {

		int result = 0;

		if (aSong != null) {

			if (getAlbum() != null && aSong.getAlbum() != null) {
				if (getAlbum().getArtist() != null && aSong.getAlbum().getArtist() != null) {
					result = ObjectUtils.compare(getAlbum().getArtist().getName(), aSong.getAlbum().getArtist().getName());
				}
				if (result == 0) {
					result = ObjectUtils.compare(getAlbum().getYear(), aSong.getAlbum().getYear());
				}
				if (result == 0) {
					result = ObjectUtils.compare(getAlbum().getName(), aSong.getAlbum().getName());
				}
			}

			if (getFile() != null && aSong.getFile() != null) {
				if (result == 0) {

					Integer discNumber1 = getFile().getDiscNumber() != null ? getFile().getDiscNumber() : 1;
					Integer discNumber2 = aSong.getFile().getDiscNumber() != null ? aSong.getFile().getDiscNumber() : 1;

					result = ObjectUtils.compare(discNumber1, discNumber2);
				}
				if (result == 0) {

					Integer trackNumber1 = getFile().getTrackNumber() != null ? getFile().getTrackNumber() : 1;
					Integer trackNumber2 = aSong.getFile().getTrackNumber() != null ? aSong.getFile().getTrackNumber() : 1;

					result = ObjectUtils.compare(trackNumber1, trackNumber2);
				}
				if (result == 0) {
					result = ObjectUtils.compare(getFile().getName(), aSong.getFile().getName());
				}
			}

		} else {
			result = -1;
		}

		return result;
	}
}
