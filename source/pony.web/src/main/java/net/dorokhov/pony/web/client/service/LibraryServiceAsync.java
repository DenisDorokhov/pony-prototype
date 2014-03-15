package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.StatusDto;

public interface LibraryServiceAsync {

	public void startScanning(AsyncCallback<Boolean> aCallback);

	public void getStatus(AsyncCallback<StatusDto> aCallback);

}
