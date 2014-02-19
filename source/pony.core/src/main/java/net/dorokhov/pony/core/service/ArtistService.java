package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.entity.Artist;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ArtistService {

	public Long getCount();

	public List<Artist> getAll();

	public Artist getById(Integer aId);

	public Artist save(Artist aSong) throws ConstraintViolationException;

	public void deleteById(Integer aId);

	public void validate(Artist aSong) throws ConstraintViolationException;

}
