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
	public File discoverArtwork(File aFolder) {

		File artwork = fetchArtwork(aFolder);

		if (artwork == null) {
			for (String artworkFolder : externalArtworkFolders) {

				artwork = fetchArtwork(new File(aFolder, artworkFolder));

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

	private File fetchArtwork(File aParent) {

		File artwork = null;

		File[] childFiles = aParent.listFiles(new FileFilter() {
			@Override
			public boolean accept(File aFile) {

				String extension = FilenameUtils.getExtension(aFile.getName()).toLowerCase();

				return externalArtworkExtensions.contains(extension);
			}
		});

		if (childFiles != null) {

			for (File file : childFiles) {

				String name = FilenameUtils.getBaseName(file.getAbsolutePath()).toLowerCase();

				if (externalArtworkNames.contains(name)) {
					artwork = file;
				}

				if (artwork != null) {
					break;
				}
			}

			if (artwork == null && childFiles.length == 1) {
				artwork = childFiles[0];
			}
		}

		return artwork;
	}

}
