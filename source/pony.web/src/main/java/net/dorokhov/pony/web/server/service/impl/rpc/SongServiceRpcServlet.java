package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.rpc.SongServiceRpc;
import net.dorokhov.pony.web.server.service.SongServiceFacade;
import net.dorokhov.pony.web.shared.SongDto;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

public class SongServiceRpcServlet extends AbstractServiceRpcServlet implements SongServiceRpc {

	private SongServiceFacade songServiceFacade;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		songServiceFacade = aContext.getBean(SongServiceFacade.class);
	}

	@Override
	public Long getCountByAlbum(Long aAlbumId) {
		return songServiceFacade.getCountByAlbum(aAlbumId);
	}

	@Override
	public Long getCountByArtist(Long aArtistId) {
		return songServiceFacade.getCountByArtist(aArtistId);
	}

	@Override
	public ArrayList<SongDto> getByAlbum(Long aAlbumId) {
		return songServiceFacade.getByAlbum(aAlbumId);
	}

	@Override
	public ArrayList<SongDto> getByArtist(Long aArtistId) {
		return songServiceFacade.getByArtist(aArtistId);
	}

	@Override
	public ArrayList<SongDto> search(String aQuery) {
		return songServiceFacade.search(aQuery);
	}

	@Override
	public SongDto getById(Long aId) {
		return songServiceFacade.getById(aId);
	}

}
