package net.dorokhov.pony.web.server.controller;

import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.exception.ConcurrentScanException;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.core.service.StoredFileService;
import net.dorokhov.pony.web.server.service.*;
import net.dorokhov.pony.web.shared.*;
import net.dorokhov.pony.web.shared.response.Response;
import net.dorokhov.pony.web.shared.response.ResponseWithResult;
import net.dorokhov.pony.web.server.view.StreamingViewRenderer;
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

	private LibraryServiceFacade libraryServiceFacade;

	private ArtistServiceFacade artistServiceFacade;

	private AlbumServiceFacade albumServiceFacade;

	private SongServiceFacade songServiceFacade;

	private SearchServiceFacade searchServiceFacade;

	private SongService songService;

	private StoredFileService storedFileService;

	@Autowired
	public void setLibraryServiceFacade(LibraryServiceFacade aLibraryServiceFacade) {
		libraryServiceFacade = aLibraryServiceFacade;
	}

	@Autowired
	public void setArtistServiceFacade(ArtistServiceFacade aArtistServiceFacade) {
		artistServiceFacade = aArtistServiceFacade;
	}

	@Autowired
	public void setAlbumServiceFacade(AlbumServiceFacade aAlbumServiceFacade) {
		albumServiceFacade = aAlbumServiceFacade;
	}

	@Autowired
	public void setSongServiceFacade(SongServiceFacade aSongServiceFacade) {
		songServiceFacade = aSongServiceFacade;
	}

	@Autowired
	public void setSearchServiceFacade(SearchServiceFacade aSearchServiceFacade) {
		searchServiceFacade = aSearchServiceFacade;
	}

	@Autowired
	public void setSongService(SongService aSongService) {
		songService = aSongService;
	}

	@Autowired
	public void setStoredFileService(StoredFileService aStoredFileService) {
		storedFileService = aStoredFileService;
	}

	@RequestMapping(value = "/artists", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<List<ArtistDto>> getArtistList() {

		try {
			return new ResponseWithResult<List<ArtistDto>>(artistServiceFacade.getAll());
		} catch (Exception e) {
			log.error("could not get artist list", e);
		}

		return new ResponseWithResult<List<ArtistDto>>();
	}

	@RequestMapping(value = "/artist/{idOrName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<ArtistDto> getArtist(@PathVariable("idOrName") String aIdOrName) {

		try {
			return new ResponseWithResult<ArtistDto>(artistServiceFacade.getByIdOrName(aIdOrName));
		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return new ResponseWithResult<ArtistDto>();
	}

	@RequestMapping(value = "/albums/{idOrName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<List<AlbumDto>> getAlbumList(@PathVariable("idOrName") String aIdOrName) {

		try {
			return new ResponseWithResult<List<AlbumDto>>(albumServiceFacade.getByArtistIdOrName(aIdOrName));
		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return new ResponseWithResult<List<AlbumDto>>();
	}

	@RequestMapping(value = "/album/{albumId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<AlbumDto> getAlbum(@PathVariable("albumId") Integer aAlbumId) {

		try {
			return new ResponseWithResult<AlbumDto>(albumServiceFacade.getById(aAlbumId));
		} catch (Exception e) {
			log.error("could not get album [{}]", aAlbumId, e);
		}

		return new ResponseWithResult<AlbumDto>();
	}

	@RequestMapping(value = "/song/{songId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<SongDto> getSong(@PathVariable("songId") Integer aSongId) {

		try {
			return new ResponseWithResult<SongDto>(songServiceFacade.getById(aSongId));
		} catch (Exception e) {
			log.error("could not get song [{}]", aSongId, e);
		}

		return new ResponseWithResult<SongDto>();
	}

	@RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<SearchDto> search(@PathVariable("query") String aQuery) {

		try {
			return new ResponseWithResult<SearchDto>(searchServiceFacade.search(aQuery));
		} catch (Exception e) {
			log.error("could not execute search query [{}]", aQuery, e);
		}

		return new ResponseWithResult<SearchDto>();
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<StatusDto> getStatus() {

		try {
			return new ResponseWithResult<StatusDto>(libraryServiceFacade.getStatus());
		} catch (Exception e) {
			log.error("could not get status", e);
		}

		return new ResponseWithResult<StatusDto>();
	}

	@RequestMapping(value = "/startScanning", method = RequestMethod.GET)
	@ResponseBody
	public Response scan() {

		try {

			libraryServiceFacade.startScanning();

			return new Response(true);

		} catch (ConcurrentScanException e) {
			log.error("library is already being scanned");
		} catch (Exception e) {
			log.error("could not run scanning", e);
		}

		return new Response(false);
	}

	@RequestMapping(value = "/songFile/{songId}", method = RequestMethod.GET)
	@ResponseBody
	public Object getSongFile(@PathVariable("songId") Integer aSongId) {

		try {

			Song song = songService.getById(aSongId);

			if (song != null) {

				InputStream stream = new FileInputStream(new File(song.getFile().getPath()));

				StreamingViewRenderer renderer = new StreamingViewRenderer();

				HashMap<String, Object> model = new HashMap<String, Object>();

				model.put(StreamingViewRenderer.DownloadConstants.CONTENT_LENGTH, song.getFile().getSize());
				model.put(StreamingViewRenderer.DownloadConstants.FILENAME, song.getFile().getName());
				model.put(StreamingViewRenderer.DownloadConstants.LAST_MODIFIED, song.getFile().getUpdateDate());
				model.put(StreamingViewRenderer.DownloadConstants.CONTENT_TYPE, song.getFile().getMimeType());
				model.put(StreamingViewRenderer.DownloadConstants.INPUT_STREAM, stream);

				return new ModelAndView(renderer, model);
			}

			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			log.error("could not get song file", e);
		}

		return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
	}

	@RequestMapping(value = "/storedFile/{storedFileId}", method = RequestMethod.GET)
	@ResponseBody
	public Object getStoredFile(@PathVariable("storedFileId") Integer aStoredFileId) {

		try {

			StoredFile storedFile = storedFileService.getById(aStoredFileId);

			if (storedFile != null) {

				File file = storedFileService.load(storedFile);

				if (file != null) {

					InputStream stream = new FileInputStream(file);

					StreamingViewRenderer renderer = new StreamingViewRenderer();

					HashMap<String, Object> model = new HashMap<String, Object>();

					model.put(StreamingViewRenderer.DownloadConstants.CONTENT_LENGTH, file.length());
					model.put(StreamingViewRenderer.DownloadConstants.FILENAME, file.getName());
					model.put(StreamingViewRenderer.DownloadConstants.LAST_MODIFIED, storedFile.getUpdateDate());
					model.put(StreamingViewRenderer.DownloadConstants.CONTENT_TYPE, storedFile.getMimeType());
					model.put(StreamingViewRenderer.DownloadConstants.INPUT_STREAM, stream);

					return new ModelAndView(renderer, model);
				}
			}

			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			log.error("could not get stored file", e);
		}

		return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
	}
}
