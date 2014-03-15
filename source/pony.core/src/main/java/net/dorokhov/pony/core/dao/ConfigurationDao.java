package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Configuration;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ConfigurationDao extends PagingAndSortingRepository<Configuration, String> {

}
