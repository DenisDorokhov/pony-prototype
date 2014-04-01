package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.service.ExternalArtworkService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

@Service
public class ExternalArtworkServiceImpl implements ExternalArtworkService {

	private Set<String> externalArtworkNames;

	private Set<String> externalArtworkFolders;

	private Set<String> externalArtworkExtensions;

	@Value("${library.externalArtworkNames}")
	public void setExternalArtworkNames(String aExternalArtworkNames) {
		externalArtworkNames = splitCommaSeparatedList(aExternalArtworkNames);
	}

	@Value("${library.externalArtworkFolders}")
	public void setExternalArtworkFolders(String aExternalArtworkFolders) {
		externalArtworkFolders = splitCommaSeparatedList(aExternalArtworkFolders);
	}

	@Value("${library.externalArtworkExtensions}")
	public void setExternalArtworkExtensions(String aExternalArtworkExtensions) {
		externalArtworkExtensions = splitCommaSeparatedList(aExternalArtworkExtensions);
	}

	@Override
	public File fetchArtwork(File aFolder) {

		File artwork = doFetchArtwork(aFolder);

		if (artwork == null) {
			for (String artworkFolder : externalArtworkFolders) {

				artwork = doFetchArtwork(new File(aFolder, artworkFolder));

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

				return externalArtworkExtensions.contains(extension);
			}
		});

		if (allowedFiles != null) {

			for (File file : allowedFiles) {

				String name = FilenameUtils.getBaseName(file.getAbsolutePath()).toLowerCase();

				if (externalArtworkNames.contains(name)) {
					artwork = file;
				}

				if (artwork != null) {
					break;
				}
			}

			if (artwork == null && allowedFiles.length > 0) {
				artwork = allowedFiles[0];
			}
		}

		return artwork;
	}

}
