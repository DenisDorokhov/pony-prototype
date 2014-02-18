package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.SongFileDao;
import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.SongFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SongFileServiceImpl extends AbstractEntityService<SongFile, SongFileDao> implements SongFileService {

	@Override
	@Transactional(readOnly = true)
	public SongFile getByPath(String aPath) {
		return dao.findByPath(aPath);
	}

	@Override
	@Transactional
	public void deleteUpdatedBefore(Date aDate) {
		dao.deleteUpdatedBefore(aDate);
	}

	@Override
	protected void normalize(SongFile aSongFile) {
		if (aSongFile.getName() != null) {
			aSongFile.setName(aSongFile.getName().trim());
		}
	}
	
}
