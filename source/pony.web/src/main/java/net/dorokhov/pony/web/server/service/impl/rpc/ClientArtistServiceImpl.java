package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.ClientArtistService;
import net.dorokhov.pony.web.server.service.ArtistServiceRemote;
import net.dorokhov.pony.web.shared.ArtistDto;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ClientArtistServiceImpl extends AbstractClientService implements ClientArtistService {

	private ArtistServiceRemote artistService;

	@Override
	protected void initWithApplicationContext(ApplicationContext aContext) {
		artistService = aContext.getBean(ArtistServiceRemote.class);
	}

	@Override
	public Long getCount() {
		return artistService.getCount();
	}

	@Override
	public List<ArtistDto> getAll() {
		return artistService.getAll();
	}

	@Override
	public ArtistDto getById(Integer aId) {
		return artistService.getById(aId);
	}

	@Override
	public ArtistDto getByName(String aName) {
		return artistService.getByName(aName);
	}

	@Override
	public ArtistDto getByIdOrName(String aNameOrId) {
		return artistService.getByIdOrName(aNameOrId);
	}

}
