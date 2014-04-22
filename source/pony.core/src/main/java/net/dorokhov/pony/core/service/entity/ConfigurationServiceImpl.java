package net.dorokhov.pony.core.service.entity;

import net.dorokhov.pony.core.dao.ConfigurationDao;
import net.dorokhov.pony.core.domain.Configuration;
import net.dorokhov.pony.core.service.ConfigurationService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfigurationServiceImpl extends AbstractEntityService<Configuration, String, ConfigurationDao> implements ConfigurationService {

	@Override
	@Transactional(readOnly = true)
	public List<Configuration> getAll() {
		return IteratorUtils.toList(dao.findAll(new Sort("id")).iterator());
	}
}
