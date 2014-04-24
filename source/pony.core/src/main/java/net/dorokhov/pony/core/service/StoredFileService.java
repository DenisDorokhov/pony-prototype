package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.StoredFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

/**
 * Stored file service.
 */
public interface StoredFileService {

	/**
	 * Command for storing StoredFile entity in the system.
	 *
	 * The idea is the following: if there is a file that needs to be stored in the system, a storage command is created.
	 * Storage command will contain all information required to store the file in the system.
	 */
	class SaveCommand {

		/**
		 * Type is used to identify what should be done with the filesystem file to be stored. It can be moved (e.g. when we
		 * created a temporary file or handled uploaded file) or copied (e.g. we want to copy some file to our system for
		 * using it).
		 */
		public static enum Type {
			COPY, MOVE
		}

		private final Type type;

		private final File file;

		private String name;

		private String mimeType;

		private String checksum;

		private String tag;

		private String userData;

		public SaveCommand(Type aType, File aFile) {

			if (aType == null) {
				throw new NullPointerException();
			}
			if (aFile == null) {
				throw new NullPointerException();
			}

			type = aType;
			file = aFile;
		}

		public Type getType() {
			return type;
		}

		public File getFile() {
			return file;
		}

		public String getName() {
			return name;
		}

		public void setName(String aName) {
			name = aName;
		}

		public String getMimeType() {
			return mimeType;
		}

		public void setMimeType(String aMimeType) {
			mimeType = aMimeType;
		}

		public String getChecksum() {
			return checksum;
		}

		public void setChecksum(String aChecksum) {
			checksum = aChecksum;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String aTag) {
			tag = aTag;
		}

		public String getUserData() {
			return userData;
		}

		public void setUserData(String aUserData) {
			userData = aUserData;
		}

		@Override
		public String toString() {
			return "StoredFileSaveCommand{" +
					"type=" + type +
					", mimeType='" + mimeType + '\'' +
					", checksum='" + checksum + '\'' +
					", file=" + file.getAbsolutePath() +
					'}';
		}
	}

	/**
	 * Gets number of stored files.
	 *
	 * @return number of stored files
	 */
	public long getCount();

	/**
	 * Gets all stored files with pagination option.
	 *
	 * @param aPageable pagination option
	 * @return page of stored files
	 */
	public Page<StoredFile> getAll(Pageable aPageable);

	/**
	 * Gets stored files by tag with pagination option.
	 *
	 * @param aTag stored file tag
	 * @param aPageable pagination option
	 * @return page of stored files with the given tag
	 */
	public Page<StoredFile> getByTag(String aTag, Pageable aPageable);

	/**
	 * Gets stored files by checksum.
	 *
	 * @param aChecksum stored file checksum
	 * @return list of stored files with the given checksum
	 */
	public List<StoredFile> getByChecksum(String aChecksum);

	/**
	 * Gets stored file by ID.
	 *
	 * @param aId stored file ID
	 * @return stored file with the given ID or null if none found
	 */
	public StoredFile getById(Long aId);

	/**
	 * Gets stored file by tag and checksum.
	 *
	 * @param aTag stored file tag
	 * @param aChecksum stored file checksum
	 * @return stored file with the given tag and checksum or null if none found
	 */
	public StoredFile getByTagAndChecksum(String aTag, String aChecksum);

	/**
	 * Gets filesystem file by stored file ID.
	 *
	 * @param aId stored file ID
	 * @return stored filesystem file
	 */
	public File getFile(Long aId);

	/**
	 * Gets filesystem file by stored file.
	 *
	 * @param aStoredFile stored file
	 * @return stored filesystem file
	 */
	public File getFile(StoredFile aStoredFile);

	/**
	 * Saves stored file with storage command.
	 *
	 * @param aCommand storage command
	 * @return saved stored file
	 */
	public StoredFile save(SaveCommand aCommand);

	/**
	 * Deletes stored file by ID.
	 *
	 * @param aId stored file ID
	 */
	public void deleteById(Long aId);

	/**
	 * Deletes all stored files.
	 */
	public void deleteAll();

}
