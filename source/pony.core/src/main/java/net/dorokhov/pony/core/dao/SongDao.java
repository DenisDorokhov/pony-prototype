package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Song;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Song DAO.
 */
public interface SongDao extends PagingAndSortingRepository<Song, Integer> {

	/**
	 * Retrieves number of songs by album ID.
	 *
	 * @param aAlbumId album ID
	 * @return number of songs with the given album ID
	 */
	public long countByAlbumId(Integer aAlbumId);

	/**
	 * Retrieves number of songs by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @return number of songs with the given artist ID
	 */
	public long countByAlbumArtistId(Integer aArtistId);

	/**
	 * Finds song by ID.
	 *
	 * @param aId song ID
	 * @return song with the given ID or null if none found
	 */
	@Query("SELECT s FROM Song s " +
			"INNER JOIN FETCH s.file " +
			"INNER JOIN FETCH s.album a " +
			"INNER JOIN FETCH a.artist " +
			"WHERE s.id = ?1")
	public Song findById(Integer aId);

	/**
	 * Finds songs by album ID.
	 *
	 * @param aAlbumId album ID
	 * @param aSort sort option
	 * @return list of songs with the given album ID
	 */
	@Query("SELECT s FROM Song s " +
			"INNER JOIN FETCH s.file " +
			"INNER JOIN FETCH s.album a " +
			"INNER JOIN FETCH a.artist " +
			"WHERE a.id = ?1")
	public List<Song> findByAlbumId(Integer aAlbumId, Sort aSort);

	/**
	 * Finds songs by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @param aSort sort option
	 * @return list of songs with the given artist ID
	 */
	@Query("SELECT s FROM Song s " +
			"INNER JOIN FETCH s.file " +
			"INNER JOIN FETCH s.album a " +
			"INNER JOIN FETCH a.artist " +
			"WHERE a.artist.id = ?1")
	public List<Song> findByAlbumArtistId(Integer aArtistId, Sort aSort);

	/**
	 * Finds song by song file ID.
	 *
	 * @param aSongFileId song file ID
	 * @return song with the given song file ID or null if none found
	 */
	@Query("SELECT s FROM Song s " +
			"INNER JOIN FETCH s.file " +
			"INNER JOIN FETCH s.album a " +
			"INNER JOIN FETCH a.artist " +
			"WHERE s.id = ?1")
	public Song findByFileId(Integer aSongFileId);

	/**
	 * Deletes song by song file ID.
	 *
	 * @param aSongFileId song file ID
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM Song s WHERE s.file.id = ?1")
	public void deleteByFileId(Integer aSongFileId);

}
