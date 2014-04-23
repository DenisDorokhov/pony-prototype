package net.dorokhov.pony.core.service;

import java.io.File;

/**
 * Artwork service discovers album artwork.
 */
public interface ArtworkService {

	/**
	 * Discovers artwork file in particular folder.
	 *
	 * @param aFolder folder that possibly contain artwork
	 * @return artwork file or null if no artwork is found
	 */
	public File discoverArtwork(File aFolder);

}
