package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Song;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface SongService {

	public Long getCountByAlbum(Integer aAlbumId);
	public Long getCountByArtist(Integer aArtistId);

	public List<Song> getByAlbum(Integer aAlbumId);
	public List<Song> getByArtist(Integer aArtistId);

	public Song getById(Integer aId);
	public Song getByFile(Integer aSongFileId);

	public Song save(Song aSong) throws ConstraintViolationException;

	public void deleteById(Integer aId);

	public void validate(Song aSong) throws ConstraintViolationException;

}
