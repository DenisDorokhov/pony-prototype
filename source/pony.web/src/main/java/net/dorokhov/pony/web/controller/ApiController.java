package net.dorokhov.pony.web.controller;

import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.domain.AlbumDto;
import net.dorokhov.pony.web.domain.ArtistDto;
import net.dorokhov.pony.web.domain.SongDto;
import net.dorokhov.pony.web.domain.response.*;
import net.dorokhov.pony.web.service.AlbumServiceRemote;
import net.dorokhov.pony.web.service.ArtistServiceRemote;
import net.dorokhov.pony.web.service.SongServiceRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiController {

	private static final String LIBRARY_PATH = "/Volumes/Volume_1/Shared/Music/Denis/Chris Rea"; // TODO: configure library path

	private final Logger log = LoggerFactory.getLogger(getClass());

	private LibraryScanner libraryScanner;

	private ArtistServiceRemote artistServiceRemote;

	private AlbumServiceRemote albumServiceRemote;

	private SongServiceRemote songServiceRemote;

	@Autowired
	public void setLibraryScanner(LibraryScanner aLibraryScanner) {
		libraryScanner = aLibraryScanner;
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

	@RequestMapping("/artistList")
	@ResponseBody
	public ArtistListResponse getArtistList() {

		ArtistListResponse response = new ArtistListResponse();

		try {

			List<ArtistDto> artistList = artistServiceRemote.getAll();

			response.setArtists(artistList);
			response.setSuccessful(true);

		} catch (Exception e) {
			log.error("could not get artist list", e);
		}

		return response;
	}

	@RequestMapping("/artist/{idOrName}")
	@ResponseBody
	public ArtistResponse getArtist(@PathVariable("idOrName") String aIdOrName) {

		ArtistResponse response = new ArtistResponse();

		try {

			ArtistDto artist = artistServiceRemote.getByIdOrName(aIdOrName);

			List<AlbumDto> albumList = null;

			if (artist != null) {
				albumList = albumServiceRemote.getByArtist(artist.getId());
			}

			response.setArtist(artist);
			response.setAlbums(albumList);
			response.setSuccessful(true);

		} catch (Exception e) {
			log.error("could not get artist [{}]", aIdOrName, e);
		}

		return response;
	}

	@RequestMapping("/album/{albumId}")
	@ResponseBody
	public AlbumResponse getAlbumList(@PathVariable("albumId") Integer aAlbumId) {

		AlbumResponse response = new AlbumResponse();

		try {

			AlbumDto album = albumServiceRemote.getById(aAlbumId);

			ArtistDto artist = null;

			if (album != null) {
				artist = artistServiceRemote.getById(album.getArtistId());
			}

			if (album != null && artist != null) {
				response.setAlbum(album);
				response.setArtist(artist);
			}
			if (album != null && artist == null) {
				log.warn("inconsistent data: no artist found for album [{}]", album.getId());
			}

			response.setSuccessful(true);

		} catch (Exception e) {
			log.error("could not get album [{}]", aAlbumId, e);
		}

		return response;
	}

	@RequestMapping("/song/{songId}")
	@ResponseBody
	public SongResponse getSong(@PathVariable("songId") Integer aSongId) {

		SongResponse response = new SongResponse();

		try {

			SongDto song = songServiceRemote.getById(aSongId);

			ArtistDto artist = null;
			AlbumDto album = null;

			if (song != null) {
				artist = artistServiceRemote.getById(song.getArtistId());
				album = albumServiceRemote.getById(song.getAlbumId());
			}

			if (song != null && album != null && artist != null) {
				response.setSong(song);
				response.setAlbum(album);
				response.setArtist(artist);
			}
			if (song != null && album == null) {
				log.warn("inconsistent data: no album found for song [{}]", song.getId());
			}
			if (song != null && artist == null) {
				log.warn("inconsistent data: no artist found for song [{}]", song.getId());
			}

			response.setSuccessful(true);

		} catch (Exception e) {
			log.error("could not get song [{}]", aSongId, e);
		}

		return response;
	}

	@RequestMapping("/status")
	@ResponseBody
	public StatusResponse getStatus() {

		StatusResponse response = new StatusResponse();

		try {

			LibraryScanner.Status status = libraryScanner.getStatus();

			response.setScanning(status.isScanning());
			response.setProgress(status.getProgress());
			response.setSuccessful(true);

		} catch (Exception e) {
			log.error("could not get status", e);
		}

		return response;
	}

	@RequestMapping("/scan")
	@ResponseBody
	public ScanResponse scan() {

		ScanResponse response = new ScanResponse();

		try {

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						libraryScanner.scan(new File(LIBRARY_PATH));
					} catch (Exception e) {
						log.error("could not scan library", e);
					}
				}
			}).start();

			response.setSuccessful(true);

		} catch (Exception e) {
			log.error("could not run scanning", e);
		}

		return response;
	}
}
