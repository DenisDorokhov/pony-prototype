package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Artist;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Artist DAO.
 */
public interface ArtistDao extends PagingAndSortingRepository<Artist, Long> {

	/**
	 * Retrieves number of artists by artwork stored file ID.
	 *
	 * @param aStoredFileId stored file ID
	 * @return number of artists with the given artwork stored file ID
	 */
	public long countByArtworkId(Long aStoredFileId);

	/**
	 * Finds artists by artwork stored file ID.
	 *
	 * @param aStoredFileId stored file ID
	 * @param aSort sort option
	 * @return list of artists with the given artwork stored file ID
	 */
	public List<Artist> findByArtworkId(Long aStoredFileId, Sort aSort);

	/**
	 * Finds artist by name.
	 *
	 * @param aName artist name
	 * @return artist with the given name or null if none found
	 */
	public Artist findByName(String aName);

}
