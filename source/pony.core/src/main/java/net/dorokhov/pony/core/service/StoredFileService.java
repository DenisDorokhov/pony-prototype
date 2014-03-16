package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.StoredFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ConstraintViolationException;

public interface StoredFileService {

	public Long getCount();

	public Page<StoredFile> getAll(Pageable aPageable);

	public StoredFile getById(Integer aId);
	public StoredFile getByPath(String aPath);

	public StoredFile save(StoredFile aStoredFile) throws ConstraintViolationException;

	public void deleteById(Integer aId);

	public void validate(StoredFile aStored1File) throws ConstraintViolationException;

}
