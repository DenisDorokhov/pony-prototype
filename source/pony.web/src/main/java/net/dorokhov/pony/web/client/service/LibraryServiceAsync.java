package net.dorokhov.pony.web.client.service;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.StatusDto;

public interface LibraryServiceAsync {

	public Request startScanning(AsyncCallback<Boolean> aCallback);

	public Request getStatus(AsyncCallback<StatusDto> aCallback);

}
