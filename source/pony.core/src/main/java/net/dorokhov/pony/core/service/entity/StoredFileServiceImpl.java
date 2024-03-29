package net.dorokhov.pony.core.service.entity;

import net.dorokhov.pony.core.dao.StoredFileDao;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.MimeTypeService;
import net.dorokhov.pony.core.service.StoredFileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class StoredFileServiceImpl extends AbstractEntityService<StoredFile, Long, StoredFileDao> implements StoredFileService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final Object lock = new Object();

	private MimeTypeService mimeTypeService;

	private String storageFolder;

	private File filesFolder;

	@Autowired
	public void setMimeTypeService(MimeTypeService aMimeTypeService) {
		mimeTypeService = aMimeTypeService;
	}

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
	public File getFile(Long aId) {
		return getFile(getById(aId));
	}

	@Override
	@Transactional(readOnly = true)
	public File getFile(StoredFile aStoredFile) {

		File file = new File(filesFolder, aStoredFile.getPath());

		if (!file.exists()) {
			throw new RuntimeException(new FileNotFoundException("File [" + file.getAbsolutePath() + "] not found."));
		}

		return file;
	}

	@Override
	@Transactional
	public StoredFile save(SaveCommand aCommand) {

		if (!aCommand.getFile().exists()) {
			throw new RuntimeException(new FileNotFoundException("File [" + aCommand.getFile().getAbsolutePath() + "] not found."));
		}
		if (aCommand.getFile().isDirectory()) {
			throw new RuntimeException("File [" + aCommand.getFile().getAbsolutePath() + "] is directory.");
		}

		File targetFile = null;

		try {

			String relativePath;

			synchronized (lock) {

				relativePath = commandToPath(aCommand);

				targetFile = new File(filesFolder, relativePath);

				switch (aCommand.getType()) {

					case COPY:
						FileUtils.copyFile(aCommand.getFile(), targetFile);
						break;

					case MOVE:
						FileUtils.moveFile(aCommand.getFile(), targetFile);
						break;

					default:
						throw new RuntimeException("Storage command type cannot be null.");
				}
			}

			StoredFile storedFile = new StoredFile();

			storedFile.setName(aCommand.getName());
			storedFile.setMimeType(aCommand.getMimeType());
			storedFile.setChecksum(aCommand.getChecksum());
			storedFile.setTag(aCommand.getTag());
			storedFile.setUserData(aCommand.getUserData());
			storedFile.setPath(relativePath);

			storedFile = save(storedFile);

			final File fileToDeleteOnRollback = targetFile;

			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCompletion(int aStatus) {
					if (aStatus != STATUS_COMMITTED) {
						fileToDeleteOnRollback.delete();
					}
				}
			});

			return storedFile;

		} catch (Exception e) {

			if (targetFile != null) {
				targetFile.delete();
			}

			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public void deleteById(Long aId) {

		StoredFile storedFile = getById(aId);

		if (storedFile != null) {

			final File file = new File(filesFolder, storedFile.getPath());

			super.deleteById(storedFile.getId());

			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					if (!file.delete()) {
						log.warn("could not delete file [{}] from file system", file.getAbsolutePath());
					}
				}
			});
		}
	}

	@Override
	@Transactional
	public void deleteAll() {

		dao.deleteAll();

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				try {
					FileUtils.cleanDirectory(filesFolder);
				} catch (Exception e) {
					log.warn("could not clean storage folder", e);
				}
			}
		});
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

	private String commandToPath(SaveCommand aCommand) {

		File file;

		int attempt = 0;

		do {

			StringBuilder buf = new StringBuilder(StringUtils.hasText(aCommand.getTag()) ? aCommand.getTag().trim() + "/" : "");

			// Don't put too many files into one folder
			buf.append(RandomStringUtils.random(2, false, true)).append("/")
					.append(RandomStringUtils.random(2, false, true)).append("/");

			// Append task name or file name
			if (aCommand.getName() != null) {

				String name = aCommand.getName();

				name = name.replaceAll("[^\\p{L}0-9\\s]", "");
				name = name.replaceAll("\\s+", "-");

				buf.append(name);

			} else {
				buf.append(aCommand.getFile().getName());
			}

			// Append attempt number (to guarantee file name to be unique)
			if (attempt > 0) {
				buf.append(attempt);
			}

			// Append type extension
			String fileExtension = mimeTypeService.getFileExtension(aCommand.getMimeType());
			if (fileExtension != null) {
				buf.append(".").append(fileExtension);
			}

			file = new File(buf.toString());

			attempt++;

		} while (file.exists());

		return file.getPath();
	}
}
