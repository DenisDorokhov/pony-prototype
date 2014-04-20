package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Song;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface SongService {

	public long getCountByAlbum(Long aAlbumId);
	public long getCountByArtist(Long aArtistId);

	public List<Song> getByAlbum(Long aAlbumId);
	public List<Song> getByArtist(Long aArtistId);

	public List<Song> search(String aQuery);

	public Song getById(Long aId);
	public Song getByFile(Long aSongFileId);

	public Song save(Song aSong) throws ConstraintViolationException;

	public void deleteById(Long aId);
	public void deleteByFileId(Long aSongFileId);

	public void validate(Song aSong) throws ConstraintViolationException;

}
