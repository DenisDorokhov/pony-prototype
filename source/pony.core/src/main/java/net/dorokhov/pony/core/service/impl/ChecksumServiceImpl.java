package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.service.ChecksumService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ChecksumServiceImpl implements ChecksumService {

	@Override
	public String calculateChecksum(File aFile) throws IOException {
		return calculateChecksum(new FileInputStream(aFile));
	}

	@Override
	public String calculateChecksum(InputStream aInputStream) throws IOException {
		return DigestUtils.md5Hex(aInputStream);
	}

	@Override
	public String calculateChecksum(byte[] aData) {
		return DigestUtils.md5Hex(aData);
	}

}
