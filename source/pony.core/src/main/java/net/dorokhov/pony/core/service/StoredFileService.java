package net.dorokhov.pony.core.service;

import net.dorokhov.pony.core.domain.StorageTask;
import net.dorokhov.pony.core.domain.StoredFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface StoredFileService {

	public long getCount();

	public Page<StoredFile> getAll(Pageable aPageable);
	public Page<StoredFile> getByTag(String aTag, Pageable aPageable);
	public List<StoredFile> getByChecksum(String aChecksum);

	public StoredFile getById(Long aId);
	public StoredFile getByTagAndChecksum(String aTag, String aChecksum);

	public File load(Long aId) throws FileNotFoundException;
	public File load(StoredFile aStoredFile) throws FileNotFoundException;

	public StoredFile save(StorageTask aTask) throws FileNotFoundException;

	public void deleteById(Long aId);
	public void deleteAll();

}
