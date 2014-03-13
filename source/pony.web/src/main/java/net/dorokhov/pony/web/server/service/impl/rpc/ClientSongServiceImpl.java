package net.dorokhov.pony.web.server.service.impl.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.dorokhov.pony.web.client.service.ClientSongService;
import net.dorokhov.pony.web.server.service.SongServiceRemote;
import net.dorokhov.pony.web.shared.SongDto;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.List;

public class ClientSongServiceImpl extends RemoteServiceServlet implements ClientSongService {

	private SongServiceRemote songService;

	@Override
	public void init(ServletConfig aConfig) throws ServletException {

		super.init(aConfig);

		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(aConfig.getServletContext());

		songService = context.getBean(SongServiceRemote.class);
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
