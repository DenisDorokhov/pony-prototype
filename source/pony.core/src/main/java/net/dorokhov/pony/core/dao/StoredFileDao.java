package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.StoredFile;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StoredFileDao extends PagingAndSortingRepository<StoredFile, Integer> {

	public StoredFile findByPath(String aPath);

}
