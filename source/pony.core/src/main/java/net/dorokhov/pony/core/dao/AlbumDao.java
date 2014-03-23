package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Album;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Album DAO.
 */
public interface AlbumDao extends PagingAndSortingRepository<Album, Integer> {

	/**
	 * Retrieves count of albums by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @return number of albums with the given artist ID
	 */
	public long countByArtistId(Integer aArtistId);

	/**
	 * Finds albums by artwork stored file ID.
	 *
	 * @param aStoredFileId stored file ID
	 * @param aSort album sort option
	 * @return list of albums with the given artwork stored file ID
	 */
	public List<Album> findByArtworkId(Integer aStoredFileId, Sort aSort);

	/**
	 * Finds albums by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @param aSort album sort option
	 * @return list of albums with the given artist ID
	 */
	public List<Album> findByArtistId(Integer aArtistId, Sort aSort);

	/**
	 * Finds album by artist ID and album name.
	 *
	 * @param aArtistId artist ID
	 * @param aName album name
	 * @return album with the given artist ID and album name or {@literal null} if none found
	 */
	public Album findByArtistIdAndName(Integer aArtistId, String aName);

}
