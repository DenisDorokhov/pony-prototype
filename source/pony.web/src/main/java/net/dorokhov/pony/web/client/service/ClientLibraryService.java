package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.StatusDto;

@RemoteServiceRelativePath("rpc/libraryService")
public interface ClientLibraryService extends RemoteService {

	public boolean startScanning();

	public StatusDto getStatus();

}
