package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.rpc.ConfigurationServiceRpc;
import net.dorokhov.pony.web.server.service.ConfigurationServiceFacade;
import net.dorokhov.pony.web.shared.ConfigurationDto;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

public class ConfigurationServiceRpcServlet extends AbstractServiceRpcServlet implements ConfigurationServiceRpc {

	private ConfigurationServiceFacade configurationServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		configurationServiceFacade = aContext.getBean(ConfigurationServiceFacade.class);
	}

	@Override
	public List<ConfigurationDto> getAll() {
		return configurationServiceFacade.getAll();
	}

	@Override
	public List<ConfigurationDto> save(List<ConfigurationDto> aConfigurations) {
		return configurationServiceFacade.save(aConfigurations);
	}
}
