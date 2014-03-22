package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.ArtistService;
import net.dorokhov.pony.web.server.service.ArtistServiceFacade;
import net.dorokhov.pony.web.shared.ArtistDto;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

public class ArtistServiceServlet extends AbstractServiceServlet implements ArtistService {

	private ArtistServiceFacade artistService;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		artistService = aContext.getBean(ArtistServiceFacade.class);
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
