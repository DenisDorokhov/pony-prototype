package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.StorageTask;
import net.dorokhov.pony.core.domain.StoredFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;

public interface StoredFileService {

	public Long getCount();

	public Page<StoredFile> getAll(Pageable aPageable);

	public StoredFile getById(Integer aId);
	public StoredFile getByTagAndChecksum(String aTag, String aChecksum);
	public StoredFile getByChecksum(String aChecksum);

	public File load(Integer aId) throws FileNotFoundException;
	public File load(StoredFile aStoredFile) throws FileNotFoundException;

	public StoredFile save(StorageTask aTask) throws FileNotFoundException;

	public void deleteById(Integer aId);
	public void deleteAll();

}
