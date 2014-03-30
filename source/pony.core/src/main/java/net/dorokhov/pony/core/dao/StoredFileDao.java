package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.StoredFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Stored file DAO.
 */
public interface StoredFileDao extends PagingAndSortingRepository<StoredFile, Integer> {

	/**
	 * Finds stored files by tag.
	 *
	 * @param aTag stored file tag
	 * @param aPageable pagination option
	 * @return page of stored files with the given tag.
	 */
	public Page<StoredFile> findByTag(String aTag, Pageable aPageable);

	/**
	 * Finds stored files by checksum.
	 *
	 * @param aChecksum stored file checksum
	 * @return list of stored files with the given checksum.
	 */
	public List<StoredFile> findByChecksum(String aChecksum);

	/**
	 * Finds stored files by tag and checksum.
	 *
	 * @param aTag stored file tag
	 * @param aChecksum stored file checksum
	 * @return stored file with the given tag and checksum or {@literal null} if none found
	 */
	public StoredFile findByTagAndChecksum(String aTag, String aChecksum);

}
