package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.AlbumService;
import net.dorokhov.pony.web.server.service.AlbumServiceFacade;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

public class AlbumServiceServlet extends AbstractServiceServlet implements AlbumService {

	private AlbumServiceFacade albumServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		albumServiceFacade = aContext.getBean(AlbumServiceFacade.class);
	}

	@Override
	public Long getCountByArtist(Integer aArtistId) {
		return albumServiceFacade.getCountByArtist(aArtistId);
	}

	@Override
	public List<AlbumSongsDto> getByArtist(Integer aArtistId) {
		return albumServiceFacade.getByArtist(aArtistId);
	}

	@Override
	public List<AlbumSongsDto> getByArtistIdOrName(String aIdOrName) {
		return albumServiceFacade.getByArtistIdOrName(aIdOrName);
	}

	@Override
	public List<AlbumDto> search(String aQuery) {
		return albumServiceFacade.search(aQuery);
	}

	@Override
	public AlbumSongsDto getById(Integer aId) {
		return albumServiceFacade.getById(aId);
	}

}
