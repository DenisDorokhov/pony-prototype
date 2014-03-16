package net.dorokhov.pony.core.domain;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class StorageTask {

	public static enum Type {
		COPY, MOVE
	}

	private final Type type;

	private final File file;

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

	public static StorageTask createWithTemporaryFile(byte[] aContents) throws IOException {
		return createWithTemporaryFile(aContents, null);
	}

	public static StorageTask createWithTemporaryFile(byte[] aContents, String aName) throws IOException {

		String fileName;

		if (aName != null) {

			aName = aName.replaceAll("[^a-zA-Z0-9\\s]", "");
			aName = aName.replaceAll("\\s+", "-");

			fileName = aName + "-" + UUID.randomUUID();

		} else {
			fileName = UUID.randomUUID().toString();
		}

		File createdFile = new File(FileUtils.getTempDirectory(), fileName);

		FileUtils.writeByteArrayToFile(createdFile, aContents);

		return new StorageTask(Type.MOVE, createdFile);
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
