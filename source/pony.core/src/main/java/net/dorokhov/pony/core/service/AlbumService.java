package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.entity.Album;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

public interface AlbumService {

	public Long getCountByArtist(Integer aArtistId);

	public List<Album> getByArtist(Integer aArtistId);

	public Album getById(Integer aId);
	public Album getByArtistAndName(Integer aArtistId, String aName);

	public Album save(Album aSong) throws ConstraintViolationException;

	public void deleteById(Integer aId);
	public void deleteUpdatedBefore(Date aDate);

	public void validate(Album aSong) throws ConstraintViolationException;

}
