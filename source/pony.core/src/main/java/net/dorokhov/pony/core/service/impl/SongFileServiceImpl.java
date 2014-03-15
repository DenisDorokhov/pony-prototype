package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.SongFileDao;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.SongFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SongFileServiceImpl extends AbstractEntityService<SongFile, Integer, SongFileDao> implements SongFileService {

	@Override
	@Transactional(readOnly = true)
	public SongFile getByPath(String aPath) {
		return dao.findByPath(aPath);
	}

	@Override
	protected void normalize(SongFile aSongFile) {
		if (aSongFile.getName() != null) {
			aSongFile.setName(aSongFile.getName().trim());
		}
	}
	
}
