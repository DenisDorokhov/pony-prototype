package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.StoredFileDao;
import net.dorokhov.pony.core.domain.StorageTask;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.StoredFileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class StoredFileServiceImpl extends AbstractEntityService<StoredFile, Integer, StoredFileDao> implements StoredFileService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Object lock = new Object();

	private String storageFolder;

	private File filesFolder;

	@Value("${storedFile.path}")
	public void setStorageFolder(String aStorageFolder) {

		storageFolder = aStorageFolder;

		createFilesFolder();
	}

	@Override
	@Transactional(readOnly = true)
	public StoredFile getByTagAndChecksum(String aTag, String aChecksum) {
		return dao.findByTagAndChecksum(aTag, aChecksum);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StoredFile> getByTag(String aTag, Pageable aPageable) {
		return dao.findByTag(aTag, aPageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StoredFile> getByChecksum(String aChecksum) {
		return dao.findByChecksum(aChecksum);
	}

	@Override
	@Transactional(readOnly = true)
	public File load(Integer aId) throws FileNotFoundException {
		return load(getById(aId));
	}

	@Override
	@Transactional(readOnly = true)
	public File load(StoredFile aStoredFile) throws FileNotFoundException {

		File file = new File(filesFolder, aStoredFile.getPath());

		if (!file.exists()) {
			throw new FileNotFoundException("File [" + file.getAbsolutePath() + "] not found.");
		}

		return file;
	}

	@Override
	@Transactional
	public StoredFile save(StorageTask aTask) throws FileNotFoundException {

		if (!aTask.getFile().exists()) {
			throw new FileNotFoundException("File [" + aTask.getFile().getAbsolutePath() + "] not found.");
		}
		if (aTask.getFile().isDirectory()) {
			throw new RuntimeException("File [" + aTask.getFile().getAbsolutePath() + "] is directory.");
		}

		File targetFile = null;

		try {

			String relativePath;

			synchronized (lock) {

				relativePath = taskToPath(aTask);

				targetFile = new File(filesFolder, relativePath);

				switch (aTask.getType()) {

					case COPY:
						FileUtils.copyFile(aTask.getFile(), targetFile);
						break;

					case MOVE:
						FileUtils.moveFile(aTask.getFile(), targetFile);
						break;

					default:
						throw new RuntimeException("Storage task type cannot be null.");
				}
			}

			StoredFile storedFile = new StoredFile();

			storedFile.setName(aTask.getFile().getName());
			storedFile.setMimeType(aTask.getMimeType());
			storedFile.setChecksum(aTask.getChecksum());
			storedFile.setTag(aTask.getTag());
			storedFile.setPath(relativePath);

			return save(storedFile);

		} catch (Exception e) {

			if (targetFile != null) {
				targetFile.delete();
			}

			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public void deleteById(Integer aId) {

		StoredFile storedFile = getById(aId);

		if (storedFile != null) {

			File file = new File(filesFolder, storedFile.getPath());

			super.deleteById(storedFile.getId());

			if (!file.delete()) {
				log.warn("could not delete file [{}] from file system", file.getAbsolutePath());
			}
		}
	}

	@Override
	@Transactional
	public void deleteAll() {

		dao.deleteAll();

		try {
			FileUtils.cleanDirectory(filesFolder);
		} catch (Exception e) {
			log.warn("could not clean storage folder", e);
		}
	}

	@Override
	protected void normalize(StoredFile aStoredFile) {
		if (aStoredFile.getName() != null) {
			aStoredFile.setName(aStoredFile.getName().trim());
		}
	}

	private void createFilesFolder() {

		File userHome = FileUtils.getUserDirectory();

		filesFolder = new File(userHome, storageFolder);

		if (!filesFolder.exists()) {
			if (!filesFolder.mkdirs()) {
				throw new RuntimeException("Could not create directory [" + filesFolder.getAbsolutePath() + "] for storing files.");
			}
		}
	}

	private String taskToPath(StorageTask aTask) {

		File file;

		int attempt = 0;

		do {

			StringBuilder buf = new StringBuilder(StringUtils.hasText(aTask.getTag()) ? aTask.getTag().trim() + "/" : "");

			String pathHash = DigestUtils.md5Hex(aTask.getFile().getAbsolutePath() + attempt);

			// Don't put too many files into one folder
			buf.append(pathHash.substring(0, 7)).append("/")
					.append(pathHash.substring(8, 15)).append("/")
					.append(pathHash.substring(16, 23)).append("/")
					.append(pathHash.substring(24, 31)).append("/");

			// Append task name or file name
			if (aTask.getName() != null) {

				String name = aTask.getName();

				name = name.replaceAll("[^a-zA-Z0-9\\s]", "");
				name = name.replaceAll("\\s+", "-");

				buf.append(name);

			} else {
				buf.append(aTask.getFile().getName());
			}

			// Append attempt number (to guarantee file name to be unique)
			if (attempt > 0) {
				buf.append(attempt);
			}

			// Append type extension
			String[] mimeTypeParts = aTask.getMimeType().split("/");
			if (mimeTypeParts.length > 1) {
				buf.append(".").append(mimeTypeParts[1]);
			}

			file = new File(buf.toString());

			attempt++;

		} while (file.exists());

		return file.getPath();
	}
}
