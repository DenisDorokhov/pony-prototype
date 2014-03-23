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
	 * Finds songs by album ID.
	 *
	 * @param aAlbumId album ID
	 * @param aSort sort option
	 * @return list of songs with the given album ID
	 */
	public List<Song> findByAlbumId(Integer aAlbumId, Sort aSort);

	/**
	 * Finds songs by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @param aSort sort option
	 * @return list of songs with the given artist ID
	 */
	public List<Song> findByAlbumArtistId(Integer aArtistId, Sort aSort);

	/**
	 * Finds song by song file ID.
	 *
	 * @param aSongFileId song file ID
	 * @return song with the given song file ID or {@literal null} if none found
	 */
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
