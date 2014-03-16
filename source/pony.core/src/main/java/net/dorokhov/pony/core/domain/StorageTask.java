package net.dorokhov.pony.core.domain;

import java.io.File;

public class StorageTask {

	public static enum Type {
		COPY, MOVE
	}

	private Type type;

	private String mimeType;

	private String checksum;

	private String tag;

	private File file;

	public Type getType() {
		return type;
	}

	public void setType(Type aType) {
		type = aType;
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

	public File getFile() {
		return file;
	}

	public void setFile(File aFile) {
		file = aFile;
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
