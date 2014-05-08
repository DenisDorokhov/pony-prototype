package net.dorokhov.pony.web.server.controller;

import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.domain.StoredFile;
import net.dorokhov.pony.core.service.SongFileService;
import net.dorokhov.pony.core.service.StoredFileService;
import net.dorokhov.pony.web.server.service.*;
import net.dorokhov.pony.web.server.view.StreamingViewRenderer;
import net.dorokhov.pony.web.shared.*;
import net.dorokhov.pony.web.shared.response.ResponseWithResult;
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

	private InstallationServiceFacade installationServiceFacade;

	private ArtistServiceFacade artistServiceFacade;

	private AlbumServiceFacade albumServiceFacade;

	private SongServiceFacade songServiceFacade;

	private SearchServiceFacade searchServiceFacade;

	private SongFileService songFileService;

	private StoredFileService storedFileService;

	@Autowired
	public void setInstallationServiceFacade(InstallationServiceFacade aInstallationServiceFacade) {
		installationServiceFacade = aInstallationServiceFacade;
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
	public void setSongFileService(SongFileService aSongFileService) {
		songFileService = aSongFileService;
	}

	@Autowired
	public void setStoredFileService(StoredFileService aStoredFileService) {
		storedFileService = aStoredFileService;
	}

	@RequestMapping(value = "/installation", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<InstallationDto> getInstallation() {

		try {
			return new ResponseWithResult<InstallationDto>(installationServiceFacade.getInstallation());
		} catch (Exception e) {
			log.error("could not get installation", e);
		}

		return new ResponseWithResult<InstallationDto>();
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
	public ResponseWithResult<List<AlbumSongsDto>> getAlbumList(@PathVariable("idOrName") String aIdOrName) {

		try {
			return new ResponseWithResult<List<AlbumSongsDto>>(albumServiceFacade.getByArtistIdOrName(aIdOrName));
		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return new ResponseWithResult<List<AlbumSongsDto>>();
	}

	@RequestMapping(value = "/album/{albumId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<AlbumSongsDto> getAlbum(@PathVariable("albumId") Long aAlbumId) {

		try {
			return new ResponseWithResult<AlbumSongsDto>(albumServiceFacade.getById(aAlbumId));
		} catch (Exception e) {
			log.error("could not get album [{}]", aAlbumId, e);
		}

		return new ResponseWithResult<AlbumSongsDto>();
	}

	@RequestMapping(value = "/song/{songId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWithResult<SongDto> getSong(@PathVariable("songId") Long aSongId) {

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

	@RequestMapping(value = "/songFile/{songFileId}", method = RequestMethod.GET)
	@ResponseBody
	public Object getSongFile(@PathVariable("songFileId") Long aSongFileId) {

		try {

			SongFile songFile = songFileService.getById(aSongFileId);

			if (songFile != null) {

				InputStream stream = new FileInputStream(new File(songFile.getPath()));

				StreamingViewRenderer renderer = new StreamingViewRenderer();

				HashMap<String, Object> model = new HashMap<String, Object>();

				model.put(StreamingViewRenderer.DownloadConstants.CONTENT_LENGTH, songFile.getSize());
				model.put(StreamingViewRenderer.DownloadConstants.FILENAME, songFile.getName());
				model.put(StreamingViewRenderer.DownloadConstants.LAST_MODIFIED, songFile.getUpdateDate());
				model.put(StreamingViewRenderer.DownloadConstants.CONTENT_TYPE, songFile.getMimeType());
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
	public Object getStoredFile(@PathVariable("storedFileId") Long aStoredFileId) {

		try {

			StoredFile storedFile = storedFileService.getById(aStoredFileId);

			if (storedFile != null) {

				File file = storedFileService.getFile(storedFile);

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
