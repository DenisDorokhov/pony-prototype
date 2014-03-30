package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;

import java.util.List;

/**
 * Search service.
 */
public interface SearchService {

	/**
	 * Creates search index.
	 */
	public void createIndex();

	/**
	 * Clears search index.
	 */
	public void clearIndex();

	/**
	 * Searches artists by query.
	 *
	 * @param aQuery search query
	 * @param aMaxResults maximal number of results to return
	 * @return list of artists satisfying the given query
	 */
	public List<Artist> searchArtists(String aQuery, int aMaxResults);

	/**
	 * Searches albums by query.
	 *
	 * @param aQuery search query
	 * @param aMaxResults maximal number of results to return
	 * @return list of albums satisfying the given query
	 */
	public List<Album> searchAlbums(String aQuery, int aMaxResults);

	/**
	 * Searches songs by query.
	 *
	 * @param aQuery search query
	 * @param aMaxResults maximal number of results to return
	 * @return list songs satisfying the given query
	 */
	public List<Song> searchSongs(String aQuery, int aMaxResults);

}
