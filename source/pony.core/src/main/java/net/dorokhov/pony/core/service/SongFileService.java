package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.entity.SongFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolationException;
import java.util.Date;

public interface SongFileService {

	public Long getCount();

	public Page<SongFile> getAll(Pageable aPageable);

	public SongFile getById(Integer aId);

	public SongFile getByPath(String aPath);

	public SongFile save(SongFile aSongFile) throws ConstraintViolationException;

	public void deleteById(Integer aId);

	public void deleteByUpdateDateBefore(Date aDate);

	public void validate(SongFile aSongFile) throws ConstraintViolationException;

}
