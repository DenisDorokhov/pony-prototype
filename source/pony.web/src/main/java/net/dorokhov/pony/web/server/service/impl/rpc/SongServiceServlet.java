package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.SongService;
import net.dorokhov.pony.web.server.service.SongServiceRemote;
import net.dorokhov.pony.web.shared.SongDto;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

public class SongServiceServlet extends AbstractServiceServlet implements SongService {

	private SongServiceRemote songService;

	@Override
	protected void initWithApplicationContext(WebApplicationContext aContext) {
		songService = aContext.getBean(SongServiceRemote.class);
	}

	@Override
	public Long getCountByAlbum(Integer aAlbumId) {
		return songService.getCountByAlbum(aAlbumId);
	}

	@Override
	public Long getCountByArtist(Integer aArtistId) {
		return songService.getCountByArtist(aArtistId);
	}

	@Override
	public List<SongDto> getByAlbum(Integer aAlbumId) {
		return songService.getByAlbum(aAlbumId);
	}

	@Override
	public List<SongDto> getByArtist(Integer aArtistId) {
		return songService.getByArtist(aArtistId);
	}

	@Override
	public SongDto getById(Integer aId) {
		return songService.getById(aId);
	}

}
