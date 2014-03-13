package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.List;

@RemoteServiceRelativePath("rpc/artistService")
public interface ClientArtistService {

	public Long getCount();

	public List<ArtistDto> getAll();

	public ArtistDto getById(Integer aId);
	public ArtistDto getByName(String aName);
	public ArtistDto getByIdOrName(String aNameOrId);

}
