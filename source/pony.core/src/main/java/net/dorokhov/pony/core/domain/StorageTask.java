package net.dorokhov.pony.core.domain;

import java.io.File;

/**
 * Task for storing StoredFile entity in the system.
 *
 * The idea is the following: if there is a file that needs to be stored in the system, a storage task is created.
 * Storage task will contain all information required to store the file in the system.
 */
public class StorageTask {

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

	public StorageTask(Type aType, File aFile) {

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
		return "StorageTask{" +
				"type=" + type +
				", mimeType='" + mimeType + '\'' +
				", checksum='" + checksum + '\'' +
				", file=" + file.getAbsolutePath() +
				'}';
	}
}
