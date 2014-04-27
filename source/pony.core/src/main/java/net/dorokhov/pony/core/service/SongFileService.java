package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.SongFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolationException;

/**
 * Song file service.
 */
public interface SongFileService {

	/**
	 * Gets number of song files.
	 *
	 * @return number of song files
	 */
	public long getCount();

	/**
	 * Gets number of song files by artwork stored file ID.
	 *
	 * @param aStoredFileId artwork stored file ID.
	 * @return number of song files with the given artwork stored file ID.
	 */
	public long getCountByArtwork(Long aStoredFileId);

	/**
	 * Gets all song files with pagination option.
	 *
	 * @param aPageable pagination option
	 * @return page of song files
	 */
	public Page<SongFile> getAll(Pageable aPageable);

	/**
	 * Gets song file by ID.
	 *
	 * @param aId song file ID
	 * @return song file with the given ID or null if none found
	 */
	public SongFile getById(Long aId);

	/**
	 * Gets song file by path.
	 *
	 * @param aPath song file path.
	 * @return song file with the given path or null if none found
	 */
	public SongFile getByPath(String aPath);

	/**
	 * Saves song file.
	 *
	 * @param aSongFile song file to save
	 * @return saved song file
	 * @throws ConstraintViolationException in case song file is not valid
	 */
	public SongFile save(SongFile aSongFile) throws ConstraintViolationException;

	/**
	 * Deletes song file by ID.
	 *
	 * @param aId song file ID
	 */
	public void deleteById(Long aId);

	/**
	 * Validates song file.
	 *
	 * @param aSongFile song file to validate
	 * @throws ConstraintViolationException in case song file is not valid
	 */
	public void validate(SongFile aSongFile) throws ConstraintViolationException;

}
