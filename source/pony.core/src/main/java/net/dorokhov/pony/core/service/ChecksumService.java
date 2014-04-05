package net.dorokhov.pony.core.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ChecksumService {

	public String calculateChecksum(File aFile) throws IOException;

	public String calculateChecksum(InputStream aInputStream) throws IOException;

	public String calculateChecksum(byte[] aData);

}
