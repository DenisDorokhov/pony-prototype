package net.dorokhov.pony.web.controller;

import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.web.domain.AlbumDto;
import net.dorokhov.pony.web.domain.ArtistDto;
import net.dorokhov.pony.web.domain.SongDto;
import net.dorokhov.pony.web.domain.StatusDto;
import net.dorokhov.pony.web.domain.response.Response;
import net.dorokhov.pony.web.domain.response.ResponseWithResult;
import net.dorokhov.pony.web.service.AlbumServiceRemote;
import net.dorokhov.pony.web.service.ArtistServiceRemote;
import net.dorokhov.pony.web.service.LibraryServiceRemote;
import net.dorokhov.pony.web.service.SongServiceRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private LibraryServiceRemote libraryServiceRemote;

	private ArtistServiceRemote artistServiceRemote;

	private AlbumServiceRemote albumServiceRemote;

	private SongServiceRemote songServiceRemote;

	@Autowired
	public void setLibraryServiceRemote(LibraryServiceRemote aLibraryServiceRemote) {
		libraryServiceRemote = aLibraryServiceRemote;
	}

	@Autowired
	public void setArtistServiceRemote(ArtistServiceRemote aArtistServiceRemote) {
		artistServiceRemote = aArtistServiceRemote;
	}

	@Autowired
	public void setAlbumServiceRemote(AlbumServiceRemote aAlbumServiceRemote) {
		albumServiceRemote = aAlbumServiceRemote;
	}

	@Autowired
	public void setSongServiceRemote(SongServiceRemote aSongServiceRemote) {
		songServiceRemote = aSongServiceRemote;
	}

	@RequestMapping("/artists")
	@ResponseBody
	public ResponseWithResult<List<ArtistDto>> getArtistList() {

		try {
			return new ResponseWithResult<List<ArtistDto>>(artistServiceRemote.getAll());
		} catch (Exception e) {
			log.error("could not get artist list", e);
		}

		return new ResponseWithResult<List<ArtistDto>>();
	}

	@RequestMapping("/artist/{idOrName}")
	@ResponseBody
	public ResponseWithResult<ArtistDto> getArtist(@PathVariable("idOrName") String aIdOrName) {

		try {
			return new ResponseWithResult<ArtistDto>(artistServiceRemote.getByIdOrName(aIdOrName));
		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return new ResponseWithResult<ArtistDto>();
	}

	@RequestMapping("/albums/{idOrName}")
	@ResponseBody
	public ResponseWithResult<List<AlbumDto>> getAlbumList(@PathVariable("idOrName") String aIdOrName) {

		try {
			return new ResponseWithResult<List<AlbumDto>>(albumServiceRemote.getByArtistIdOrName(aIdOrName));
		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return new ResponseWithResult<List<AlbumDto>>();
	}

	@RequestMapping("/album/{albumId}")
	@ResponseBody
	public ResponseWithResult<AlbumDto> getAlbum(@PathVariable("albumId") Integer aAlbumId) {

		try {
			return new ResponseWithResult<AlbumDto>(albumServiceRemote.getById(aAlbumId));
		} catch (Exception e) {
			log.error("could not get album [{}]", aAlbumId, e);
		}

		return new ResponseWithResult<AlbumDto>();
	}

	@RequestMapping("/song/{songId}")
	@ResponseBody
	public ResponseWithResult<SongDto> getSong(@PathVariable("songId") Integer aSongId) {

		try {
			return new ResponseWithResult<SongDto>(songServiceRemote.getById(aSongId));
		} catch (Exception e) {
			log.error("could not get song [{}]", aSongId, e);
		}

		return new ResponseWithResult<SongDto>();
	}

	@RequestMapping("/status")
	@ResponseBody
	public ResponseWithResult<StatusDto> getStatus() {

		try {
			return new ResponseWithResult<StatusDto>(libraryServiceRemote.getStatus());
		} catch (Exception e) {
			log.error("could not get status", e);
		}

		return new ResponseWithResult<StatusDto>();
	}

	@RequestMapping("/scan")
	@ResponseBody
	public Response scan() {

		try {

			libraryServiceRemote.startScanning();

			return new Response(true);

		} catch (ConcurrentScanException e) {
			log.error("library is already being scanned");
		} catch (Exception e) {
			log.error("could not run scanning", e);
		}

		return new Response(false);
	}
}
