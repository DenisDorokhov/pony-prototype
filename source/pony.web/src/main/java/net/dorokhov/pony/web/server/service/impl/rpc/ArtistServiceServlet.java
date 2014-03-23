package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.ArtistService;
import net.dorokhov.pony.web.server.service.ArtistServiceFacade;
import net.dorokhov.pony.web.shared.ArtistDto;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

public class ArtistServiceServlet extends AbstractServiceServlet implements ArtistService {

	private ArtistServiceFacade artistServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		artistServiceFacade = aContext.getBean(ArtistServiceFacade.class);
	}

	@Override
	public Long getCount() {
		return artistServiceFacade.getCount();
	}

	@Override
	public List<ArtistDto> getAll() {
		return artistServiceFacade.getAll();
	}

	@Override
	public List<ArtistDto> search(String aQuery) {
		return artistServiceFacade.search(aQuery);
	}

	@Override
	public ArtistDto getById(Integer aId) {
		return artistServiceFacade.getById(aId);
	}

	@Override
	public ArtistDto getByName(String aName) {
		return artistServiceFacade.getByName(aName);
	}

	@Override
	public ArtistDto getByIdOrName(String aNameOrId) {
		return artistServiceFacade.getByIdOrName(aNameOrId);
	}

}
