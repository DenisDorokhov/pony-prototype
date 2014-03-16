package net.dorokhov.pony.core.domain;

public class SongArtwork {

	private byte[] binaryData;

	private String mimeType;

	public SongArtwork() {}

	public SongArtwork(byte[] aBinaryData, String aMimeType) {
		binaryData = aBinaryData;
		mimeType = aMimeType;
	}

	public byte[] getBinaryData() {
		return binaryData;
	}

	public void setBinaryData(byte[] aBinaryData) {
		binaryData = aBinaryData;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String aMimeType) {
		mimeType = aMimeType;
	}
}
