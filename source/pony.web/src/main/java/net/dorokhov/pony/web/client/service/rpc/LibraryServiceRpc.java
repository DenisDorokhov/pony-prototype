package net.dorokhov.pony.web.client.service.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.StatusDto;
import net.dorokhov.pony.web.shared.exception.ConcurrentScanException;
import net.dorokhov.pony.web.shared.exception.LibraryNotDefinedException;

@RemoteServiceRelativePath("rpc/libraryService")
public interface LibraryServiceRpc extends RemoteService {

	public void startScanning() throws ConcurrentScanException, LibraryNotDefinedException;

	public StatusDto getStatus();

}
