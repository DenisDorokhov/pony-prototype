package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;

@RemoteServiceRelativePath("rpc/artistService")
public interface ArtistService extends RemoteService {

	public Long getCount();

	public ArrayList<ArtistDto> getAll();

	public ArrayList<ArtistDto> search(String aQuery);

	public ArtistDto getById(Integer aId);
	public ArtistDto getByName(String aName);
	public ArtistDto getByIdOrName(String aNameOrId);

}
