package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.dao.SongDao;
import net.dorokhov.pony.core.entity.Song;
import net.dorokhov.pony.core.entity.SongFile;
import net.dorokhov.pony.core.service.SongService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

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

	@Override
	@Transactional
	public Song save(SongFile aSongFile) throws ConstraintViolationException {

		// TODO: implement

		return null;
	}
}
