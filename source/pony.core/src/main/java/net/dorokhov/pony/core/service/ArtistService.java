package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface ArtistService {

	public Long getCount();

	public List<Artist> getAll();
	public Page<Artist> getAll(Pageable aPageable);

	public Artist getById(Integer aId);
	public Artist getByName(String aName);

	public Artist save(Artist aSong) throws ConstraintViolationException;

	public void deleteById(Integer aId);

	public void validate(Artist aSong) throws ConstraintViolationException;

}
