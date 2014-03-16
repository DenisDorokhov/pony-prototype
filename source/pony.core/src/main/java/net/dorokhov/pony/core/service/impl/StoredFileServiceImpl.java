package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.StoredFileDao;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.StoredFileService;
import org.springframework.stereotype.Service;

@Service
public class StoredFileServiceImpl extends AbstractEntityService<StoredFile, Integer, StoredFileDao> implements StoredFileService {

	@Override
	public StoredFile getByPath(String aPath) {
		return null;
	}

	@Override
	protected void normalize(StoredFile aStoredFile) {
		if (aStoredFile.getName() != null) {
			aStoredFile.setName(aStoredFile.getName().trim());
		}
	}
}
