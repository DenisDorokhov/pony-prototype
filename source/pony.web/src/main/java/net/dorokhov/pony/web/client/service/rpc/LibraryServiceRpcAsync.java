package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.ScanResultDto;
import net.dorokhov.pony.web.shared.ScanStatusDto;

public interface LibraryServiceRpcAsync {

	public Request startScanning(AsyncCallback<Void> aCallback);

	public Request getStatus(AsyncCallback<ScanStatusDto> aCallback);

	public Request getLastResult(AsyncCallback<ScanResultDto> aCallback);

}
