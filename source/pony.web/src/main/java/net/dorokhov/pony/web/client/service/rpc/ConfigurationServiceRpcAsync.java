package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.ConfigurationDto;

import java.util.List;

public interface ConfigurationServiceRpcAsync {

	public Request getAll(AsyncCallback<List<ConfigurationDto>> aCallback);

	public Request save(List<ConfigurationDto> aConfigurations, AsyncCallback<List<ConfigurationDto>> aCallback);

}
