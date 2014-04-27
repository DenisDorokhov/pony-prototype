package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SongDto;

public interface PlayList {

	public boolean hasPrevious();
	public boolean hasNext();

	public void previous(AsyncCallback<SongDto> aCallback);
	public void next(AsyncCallback<SongDto> aCallback);

	public void reset();

}
