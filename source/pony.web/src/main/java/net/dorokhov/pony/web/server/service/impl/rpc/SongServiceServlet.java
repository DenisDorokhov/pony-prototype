package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.SongService;
import net.dorokhov.pony.web.server.service.SongServiceFacade;
import net.dorokhov.pony.web.shared.SongDto;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

public class SongServiceServlet extends AbstractServiceServlet implements SongService {

	private SongServiceFacade songServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		songServiceFacade = aContext.getBean(SongServiceFacade.class);
	}

	@Override
	public Long getCountByAlbum(Integer aAlbumId) {
		return songServiceFacade.getCountByAlbum(aAlbumId);
	}

	@Override
	public Long getCountByArtist(Integer aArtistId) {
		return songServiceFacade.getCountByArtist(aArtistId);
	}

	@Override
	public List<SongDto> getByAlbum(Integer aAlbumId) {
		return songServiceFacade.getByAlbum(aAlbumId);
	}

	@Override
	public List<SongDto> getByArtist(Integer aArtistId) {
		return songServiceFacade.getByArtist(aArtistId);
	}

	@Override
	public List<SongDto> search(String aQuery) {
		return songServiceFacade.search(aQuery);
	}

	@Override
	public SongDto getById(Integer aId) {
		return songServiceFacade.getById(aId);
	}

}
