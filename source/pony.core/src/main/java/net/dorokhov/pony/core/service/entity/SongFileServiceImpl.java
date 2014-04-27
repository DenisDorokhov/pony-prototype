package net.dorokhov.pony.core.service.entity;

import net.dorokhov.pony.core.dao.SongFileDao;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.SongFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SongFileServiceImpl extends AbstractEntityService<SongFile, Long, SongFileDao> implements SongFileService {

	@Override
	@Transactional(readOnly = true)
	public long getCountByArtwork(Long aStoredFileId) {
		return dao.countByArtworkId(aStoredFileId);
	}

	@Override
	@Transactional(readOnly = true)
	public SongFile getByPath(String aPath) {
		return dao.findByPath(aPath);
	}

}
