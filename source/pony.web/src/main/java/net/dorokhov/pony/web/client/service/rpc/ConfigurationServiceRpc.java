package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.ConfigurationDto;

import java.util.List;

@RemoteServiceRelativePath("rpc/configurationService")
public interface ConfigurationServiceRpc extends RemoteService {

	public List<ConfigurationDto> getAll();

	public List<ConfigurationDto> save(List<ConfigurationDto> aConfigurations);

}
