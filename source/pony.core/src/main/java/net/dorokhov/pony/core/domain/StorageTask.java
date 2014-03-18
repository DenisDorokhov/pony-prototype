package net.dorokhov.pony.core.domain;

import java.io.File;

public class StorageTask {

	public static enum Type {
		COPY, MOVE
	}

	private final Type type;

	private final File file;

	private String name;

	private String mimeType;

	private String checksum;

	private String tag;

	public StorageTask(Type aType, File aFile) {
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

	@Override
	public String toString() {
		return "StorageTask{" +
				"type=" + type +
				", mimeType='" + mimeType + '\'' +
				", checksum='" + checksum + '\'' +
				", file=" + (file != null ? file.getAbsolutePath() : null) +
				'}';
	}
}
