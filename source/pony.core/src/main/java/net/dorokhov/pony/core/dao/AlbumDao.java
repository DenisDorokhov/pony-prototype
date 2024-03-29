package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Album;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Album DAO.
 */
public interface AlbumDao extends PagingAndSortingRepository<Album, Long> {

	/**
	 * Retrieves count of albums by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @return number of albums with the given artist ID
	 */
	public long countByArtistId(Long aArtistId);

	/**
	 * Retrieves number of albums by artwork stored file ID.
	 *
	 * @param aStoredFileId stored file ID
	 * @return number of albums with the given artwork stored file ID
	 */
	public long countByArtworkId(Long aStoredFileId);

	/**
	 * Finds album by ID.
	 *
	 * @param aId album ID
	 * @return song with the given ID or null if none found
	 */
	@Query("SELECT a FROM Album a " +
			"INNER JOIN FETCH a.artist " +
			"WHERE a.id = ?1")
	public Album findById(Long aId);

	/**
	 * Finds albums by artwork stored file ID.
	 *
	 * @param aStoredFileId stored file ID
	 * @param aSort album sort option
	 * @return list of albums with the given artwork stored file ID
	 */
	public List<Album> findByArtworkId(Long aStoredFileId, Sort aSort);

	/**
	 * Finds albums by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @param aSort album sort option
	 * @return list of albums with the given artist ID
	 */
	public List<Album> findByArtistId(Long aArtistId, Sort aSort);

	/**
	 * Finds album by artist ID and album name.
	 *
	 * @param aArtistId artist ID
	 * @param aName album name
	 * @return album with the given artist ID and album name or null if none found
	 */
	public Album findByArtistIdAndName(Long aArtistId, String aName);

}
