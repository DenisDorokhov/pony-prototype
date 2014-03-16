package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.StoredFileDao;
import net.dorokhov.pony.core.domain.StorageTask;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.StoredFileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class StoredFileServiceImpl extends AbstractEntityService<StoredFile, Integer, StoredFileDao> implements StoredFileService {

	private static final String STORAGE_RELATIVE_PATH = ".pony/stored_file";

	private final Object lock = new Object();

	private final File storageFolder;

	public StoredFileServiceImpl() {

		File userHome = FileUtils.getUserDirectory();

		storageFolder = new File(userHome, STORAGE_RELATIVE_PATH);

		if (!storageFolder.exists()) {
			if (!storageFolder.mkdirs()) {
				throw new RuntimeException("Could not create directory [" + storageFolder.getAbsolutePath() + "] for storing files.");
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public StoredFile getByTagAndChecksum(String aTag, String aChecksum) {
		return dao.findByTagAndChecksum(aTag, aChecksum);
	}

	@Override
	@Transactional(readOnly = true)
	public StoredFile getByChecksum(String aChecksum) {
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

		File file = new File(storageFolder, aStoredFile.getPath());

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

				targetFile = new File(storageFolder, relativePath);

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
			storedFile.setPath(relativePath);

			return save(storedFile);

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (targetFile != null) {
				targetFile.delete();
			}
		}
	}

	@Override
	protected void normalize(StoredFile aStoredFile) {
		if (aStoredFile.getName() != null) {
			aStoredFile.setName(aStoredFile.getName().trim());
		}
	}

	private String taskToPath(StorageTask aTask) {

		File file;

		int attempt = 0;

		do {

			String pathHash = DigestUtils.md5Hex(aTask.getFile().getAbsolutePath() + attempt);

			StringBuilder buf = new StringBuilder();

			for (int i = 0; i < pathHash.length(); i++) {
				buf.append(pathHash.charAt(i)).append("/");
			}

			buf.append(aTask.getFile().getName());

			file = new File(buf.toString());

			attempt++;

		} while (file.exists());

		return file.getPath();
	}
}
