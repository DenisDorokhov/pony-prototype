package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.SongDao;
import net.dorokhov.pony.core.entity.Song;
import net.dorokhov.pony.core.service.SongService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SongServiceImpl extends AbstractEntityService<Song, SongDao> implements SongService {

	@Override
	@Transactional(readOnly = true)
	public Long getCountByAlbum(Integer aAlbumId) {
		return dao.countByAlbumArtistId(aAlbumId);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getCountByArtist(Integer aArtistId) {
		return dao.countByAlbumArtistId(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Song> getByAlbum(Integer aAlbumId) {
		return dao.findByAlbumId(aAlbumId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Song> getByArtist(Integer aArtistId) {
		return dao.findByAlbumArtistId(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public Song getByFile(Integer aSongFileId) {
		return dao.findByFileId(aSongFileId);
	}
}
