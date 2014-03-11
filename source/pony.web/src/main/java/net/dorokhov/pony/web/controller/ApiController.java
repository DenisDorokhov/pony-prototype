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
import net.dorokhov.pony.web.view.StreamingViewRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
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

	@RequestMapping(value = "/artists", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<List<ArtistDto>> getArtistList() {

		try {
			return new ResponseWithResult<List<ArtistDto>>(artistServiceRemote.getAll());
		} catch (Exception e) {
			log.error("could not get artist list", e);
		}

		return new ResponseWithResult<List<ArtistDto>>();
	}

	@RequestMapping(value = "/artist/{idOrName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<ArtistDto> getArtist(@PathVariable("idOrName") String aIdOrName) {

		try {
			return new ResponseWithResult<ArtistDto>(artistServiceRemote.getByIdOrName(aIdOrName));
		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return new ResponseWithResult<ArtistDto>();
	}

	@RequestMapping(value = "/albums/{idOrName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<List<AlbumDto>> getAlbumList(@PathVariable("idOrName") String aIdOrName) {

		try {
			return new ResponseWithResult<List<AlbumDto>>(albumServiceRemote.getByArtistIdOrName(aIdOrName));
		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return new ResponseWithResult<List<AlbumDto>>();
	}

	@RequestMapping(value = "/album/{albumId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<AlbumDto> getAlbum(@PathVariable("albumId") Integer aAlbumId) {

		try {
			return new ResponseWithResult<AlbumDto>(albumServiceRemote.getById(aAlbumId));
		} catch (Exception e) {
			log.error("could not get album [{}]", aAlbumId, e);
		}

		return new ResponseWithResult<AlbumDto>();
	}

	@RequestMapping(value = "/song/{songId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<SongDto> getSong(@PathVariable("songId") Integer aSongId) {

		try {
			return new ResponseWithResult<SongDto>(songServiceRemote.getById(aSongId));
		} catch (Exception e) {
			log.error("could not get song [{}]", aSongId, e);
		}

		return new ResponseWithResult<SongDto>();
	}

	@RequestMapping(value = "/songFile/{songId}", method = RequestMethod.GET)
	@ResponseBody
	public Object getFile(@PathVariable("songId") Integer aSongId) {

		try {

			SongDto song = songServiceRemote.getById(aSongId);

			if (song != null) {

				InputStream stream = new FileInputStream(new File(song.getPath()));

				StreamingViewRenderer renderer = new StreamingViewRenderer();

				HashMap<String, Object> model = new HashMap<String, Object>();

				model.put(StreamingViewRenderer.DownloadConstants.CONTENT_LENGTH, song.getSize());
				model.put(StreamingViewRenderer.DownloadConstants.FILENAME, song.getName());
				model.put(StreamingViewRenderer.DownloadConstants.LAST_MODIFIED, song.getUpdateDate());
				model.put(StreamingViewRenderer.DownloadConstants.CONTENT_TYPE, "audio/mpeg3");
				model.put(StreamingViewRenderer.DownloadConstants.INPUT_STREAM, stream);

				return new ModelAndView(renderer, model);

			} else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			log.error("could not get song file", e);
		}

		return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<StatusDto> getStatus() {

		try {
			return new ResponseWithResult<StatusDto>(libraryServiceRemote.getStatus());
		} catch (Exception e) {
			log.error("could not get status", e);
		}

		return new ResponseWithResult<StatusDto>();
	}

	@RequestMapping(value = "/startScanning", method = RequestMethod.GET)
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
