package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.SongFileDao;
import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.SongFileService;
import org.springframework.stereotype.Service;

@Service
public class SongFileServiceImpl extends AbstractEntityService<SongFile, SongFileDao> implements SongFileService {

	@Override
	protected void normalize(SongFile aSongFile) {
		if (aSongFile.getName() != null) {
			aSongFile.setName(aSongFile.getName().trim());
		}
	}
	
}
