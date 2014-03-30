package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Album service.
 */
public interface AlbumService {

	/**
	 * Gets number of albums by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @return number of albums with the given artist ID
	 */
	public long getCountByArtist(Integer aArtistId);

	/**
	 * Gets all albums with pagination option.
	 *
	 * @param aPageable pagination option
	 * @return page of albums
	 */
	public Page<Album> getAll(Pageable aPageable);

	/**
	 * Gets albums by artwork stored file ID.
	 *
	 * @param aStoredFileId artwork stored file ID
	 * @return list of albums with the given artwork stored file ID.
	 */
	public List<Album> getByArtwork(Integer aStoredFileId);

	/**
	 * Gets albums by artist ID.
	 *
	 * @param aArtistId artist ID
	 * @return list of albums with the given artist ID.
	 */
	public List<Album> getByArtist(Integer aArtistId);

	/**
	 * Searches for albums.
	 *
	 * @param aQuery search query
	 * @return list of albums satisfying the given search query
	 */
	public List<Album> search(String aQuery);

	/**
	 * Gets album by ID.
	 *
	 * @param aId album ID.
	 * @return album with the given ID or {@literal null} if none found
	 */
	public Album getById(Integer aId);

	/**
	 * Get album by artist ID and album name.
	 *
	 * @param aArtistId artist ID
	 * @param aName album name
	 * @return album with the given artist ID and album name or {@literal null} if none found
	 */
	public Album getByArtistAndName(Integer aArtistId, String aName);

	/**
	 * Saves album.
	 *
	 * @param aSong song to save
	 * @return saved song
	 * @throws ConstraintViolationException in case song is not valid
	 */
	public Album save(Album aSong) throws ConstraintViolationException;

	/**
	 * Deletes album by ID.
	 *
	 * @param aId album ID.
	 */
	public void deleteById(Integer aId);

	/**
	 * Validates song.
	 *
	 * @param aSong song to validate
	 * @throws ConstraintViolationException in case song is not valid
	 */
	public void validate(Album aSong) throws ConstraintViolationException;

}
