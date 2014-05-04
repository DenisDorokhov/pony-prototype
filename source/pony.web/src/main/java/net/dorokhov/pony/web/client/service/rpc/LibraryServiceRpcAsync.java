package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.StatusDto;

public interface LibraryServiceRpcAsync {

	public Request startScanning(AsyncCallback<Void> aCallback);

	public Request getStatus(AsyncCallback<StatusDto> aCallback);

}
