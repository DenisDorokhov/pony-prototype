package net.dorokhov.pony.core.service.common;

import net.dorokhov.pony.core.service.ArtworkService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ArtworkServiceImpl implements ArtworkService {

	private Set<String> artworkFileNames = new HashSet<String>();

	private Set<String> artworkFolderNames = new HashSet<String>();

	private Set<String> artworkFileExtensions = new HashSet<String>();

	public Set<String> getArtworkFileNames() {
		return new HashSet<String>(artworkFileNames);
	}

	public void setArtworkFileNames(Set<String> aArtworkFileNames) {
		artworkFileNames = new HashSet<String>(aArtworkFileNames);
	}

	public Set<String> getArtworkFolderNames() {
		return new HashSet<String>(artworkFolderNames);
	}

	public void setArtworkFolderNames(Set<String> aArtworkFolderNames) {
		artworkFolderNames = new HashSet<String>(aArtworkFolderNames);
	}

	public Set<String> getArtworkFileExtensions() {
		return new HashSet<String>(artworkFileExtensions);
	}

	public void setArtworkFileExtensions(Set<String> aArtworkFileExtensions) {
		artworkFileExtensions = new HashSet<String>(aArtworkFileExtensions);
	}

	@Value("${library.artworkFileNames}")
	public void setArtworkFileNames(String aArtworkFileNames) {
		setArtworkFileNames(splitCommaSeparatedList(aArtworkFileNames));
	}

	@Value("${library.artworkFolderNames}")
	public void setArtworkFolderNames(String aArtworkFolderNames) {
		setArtworkFolderNames(splitCommaSeparatedList(aArtworkFolderNames));
	}

	@Value("${library.artworkFileExtensions}")
	public void setArtworkFileExtensions(String aArtworkFileExtensions) {
		setArtworkFileExtensions(splitCommaSeparatedList(aArtworkFileExtensions));
	}

	@Override
	public File discoverArtwork(File aFolder) {

		File artwork = doFetchArtwork(aFolder);

		if (artwork == null) {

			File[] folderFiles = aFolder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File aFile) {
					return artworkFolderNames.contains(aFile.getName().toLowerCase());
				}
			});

			for (File file : folderFiles) {

				artwork = doFetchArtwork(file);

				if (artwork != null) {
					break;
				}
			}
		}

		return artwork;
	}

	private Set<String> splitCommaSeparatedList(String aString) {

		Set<String> result = new HashSet<String>();

		for (String item : aString.split(",")) {

			item = item.trim();

			if (item.length() > 0) {
				result.add(item.toLowerCase());
			}
		}

		return result;
	}

	private File doFetchArtwork(File aParent) {

		File artwork = null;

		File[] allowedFiles = aParent.listFiles(new FileFilter() {
			@Override
			public boolean accept(File aFile) {

				String extension = FilenameUtils.getExtension(aFile.getName()).toLowerCase();

				return !aFile.getName().startsWith(".") && artworkFileExtensions.contains(extension);
			}
		});

		if (allowedFiles != null) {

			for (File file : allowedFiles) {

				String name = FilenameUtils.getBaseName(file.getAbsolutePath()).toLowerCase();

				if (artworkFileNames.contains(name)) {
					artwork = file;
				}

				if (artwork != null) {
					break;
				}
			}

			if (artwork == null && allowedFiles.length > 0) {

				Arrays.sort(allowedFiles);

				artwork = allowedFiles[0];
			}
		}

		return artwork;
	}

}
