package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Artist service.
 */
public interface ArtistService {

	/**
	 * Gets number of artists.
	 *
	 * @return number of artists
	 */
	public long getCount();

	/**
	 * Gets number of artists by artwork stored file ID.
	 *
	 * @param aStoredFileId artwork stored file ID.
	 * @return number of artists with the given artwork stored file ID.
	 */
	public long getCountByArtwork(Long aStoredFileId);

	/**
	 * Gets all artists.
	 *
	 * @return list of all artists
	 */
	public List<Artist> getAll();

	/**
	 * Gets artists by artwork stored file ID.
	 *
	 * @param aStoredFileId artwork stored file ID
	 * @return list of artists with the given artwork stored file ID
	 */
	public List<Artist> getByArtwork(Long aStoredFileId);

	/**
	 * Gets all artists with pagination option.
	 *
	 * @param aPageable pagination option
	 * @return page of artists
	 */
	public Page<Artist> getAll(Pageable aPageable);

	/**
	 * Searches for artists.
	 *
	 * @param aQuery search query
	 * @return list of artists satisfying the given search query
	 */
	public List<Artist> search(String aQuery);

	/**
	 * Gets artist by ID.
	 *
	 * @param aId artist ID
	 * @return artist with the given ID or null if none found
	 */
	public Artist getById(Long aId);

	/**
	 * Gets artist by name.
	 *
	 * @param aName artist name
	 * @return artist with the given name or null if none found
	 */
	public Artist getByName(String aName);

	/**
	 * Saves artist.
	 *
	 * @param aArtist artist to save
	 * @return saved artist
	 * @throws ConstraintViolationException in case artist is not valid
	 */
	public Artist save(Artist aArtist) throws ConstraintViolationException;

	/**
	 * Deletes artist by ID.
	 *
	 * @param aId artist ID
	 */
	public void deleteById(Long aId);

	/**
	 * Validates artist.
	 *
	 * @param aArtist artist to validate
	 * @throws ConstraintViolationException in case artist is not valid
	 */
	public void validate(Artist aArtist) throws ConstraintViolationException;

}
