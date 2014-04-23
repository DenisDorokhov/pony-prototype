package net.dorokhov.pony.core.service;

import java.io.File;

/**
 * External artwork service discovers artwork file in some folder.
 *
 * External artwork is non-embedded artwork i.e. artwork that is stored outside of audio file.
 */
public interface ExternalArtworkService {

	/**
	 * Discovers artwork in particular folder.
	 *
	 * @param aFolder folder that possibly contain artwork
	 * @return artwork file or null if no artwork is found
	 */
	public File fetchArtwork(File aFolder);

}
