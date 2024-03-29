package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.SongFile;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Song file DAO.
 */
public interface SongFileDao extends PagingAndSortingRepository<SongFile, Long> {

	/**
	 * Retrieves number of song files by artwork stored file ID.
	 *
	 * @param aStoredFileId stored file ID
	 * @return number of song files with the given artwork stored file ID
	 */
	public long countByArtworkId(Long aStoredFileId);

	/**
	 * Finds song file by file path.
	 *
	 * @param aPath file path
	 * @return song file with the given file path or null if none found
	 */
	public SongFile findByPath(String aPath);

}
