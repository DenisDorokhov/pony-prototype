package net.dorokhov.pony.web.server.service.impl.rpc;

import net.dorokhov.pony.web.client.service.ClientAlbumService;
import net.dorokhov.pony.web.server.service.AlbumServiceRemote;
import net.dorokhov.pony.web.shared.AlbumDto;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ClientAlbumServiceImpl extends AbstractClientService implements ClientAlbumService {

	private AlbumServiceRemote albumService;

	@Override
	protected void initWithApplicationContext(ApplicationContext aContext) {
		albumService = aContext.getBean(AlbumServiceRemote.class);
	}

	@Override
	public Long getCountByArtist(Integer aArtistId) {
		return albumService.getCountByArtist(aArtistId);
	}

	@Override
	public List<AlbumDto> getByArtist(Integer aArtistId) {
		return albumService.getByArtist(aArtistId);
	}

	@Override
	public List<AlbumDto> getByArtistIdOrName(String aIdOrName) {
		return albumService.getByArtistIdOrName(aIdOrName);
	}

	@Override
	public AlbumDto getById(Integer aId) {
		return albumService.getById(aId);
	}

}
