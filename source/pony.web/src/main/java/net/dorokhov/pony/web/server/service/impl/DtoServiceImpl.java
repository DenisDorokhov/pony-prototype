package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.server.service.DtoService;
import net.dorokhov.pony.web.shared.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@Service
public class DtoServiceImpl implements DtoService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public StatusDto statusToDto(LibraryScanner.Status aStatus) {

		StatusDto dto = new StatusDto();

		dto.setProgress(aStatus.getProgress());
		dto.setDescription(aStatus.getDescription());
		dto.setStep(aStatus.getStep());
		dto.setTotalSteps(aStatus.getTotalSteps());

		if (aStatus.getTargetFiles() != null) {
			for (File file : aStatus.getTargetFiles()) {
				dto.getTargetFiles().add(file.getAbsolutePath());
			}
		}

		return dto;
	}

	public ArtistDto artistToDto(Artist aArtist) {

		ArtistDto dto = new ArtistDto();

		dto.setId(aArtist.getId());
		dto.setCreationDate(aArtist.getCreationDate());
		dto.setUpdateDate(aArtist.getUpdateDate());
		dto.setGeneration(aArtist.getVersion());

		dto.setName(aArtist.getName());
		dto.setArtwork(aArtist.getArtwork() != null ? aArtist.getArtwork().getId() : null);
		dto.setArtworkUrl(getStoredFileUrl(dto.getArtwork()));

		return dto;
	}

	public AlbumDto albumToDto(Album aAlbum) {

		AlbumDto dto = new AlbumDto();

		initAlbumDto(dto, aAlbum);

		return dto;
	}

	public AlbumSongsDto albumToSongsDto(Album aAlbum, List<Song> aSongs) {

		AlbumSongsDto dto = new AlbumSongsDto();

		initAlbumDto(dto, aAlbum);

		for (Song song : aSongs) {
			dto.getSongs().add(songToDto(song));
		}

		return dto;
	}

	public SongDto songToDto(Song aSong) {

		SongDto dto = new SongDto();

		dto.setId(aSong.getId());
		dto.setCreationDate(aSong.getCreationDate());
		dto.setUpdateDate(aSong.getUpdateDate());
		dto.setGeneration(aSong.getVersion());

		Album album = aSong.getAlbum();

		if (album != null) {

			dto.setAlbumId(album.getId());
			dto.setAlbumName(album.getName());
			dto.setAlbumArtwork(album.getArtwork() != null ? album.getArtwork().getId() : null);
			dto.setAlbumArtworkUrl(getStoredFileUrl(dto.getAlbumArtwork()));
			dto.setAlbumYear(album.getYear());

			Artist artist = album.getArtist();

			if (artist != null) {
				dto.setArtistId(artist.getId());
				dto.setArtistName(artist.getName());
				dto.setArtistArtwork(artist.getArtwork() != null ? artist.getArtwork().getId() : null);
				dto.setArtistArtworkUrl(getStoredFileUrl(dto.getArtistArtwork()));
			}
		}

		SongFile file = aSong.getFile();

		if (file != null) {

			dto.setFile(file.getId());
			dto.setFileUrl(getSongFileUrl(file.getId()));

			dto.setPath(file.getPath());
			dto.setFormat(file.getFormat());
			dto.setMimeType(file.getMimeType());
			dto.setSize(file.getSize());
			dto.setDuration(file.getDuration());
			dto.setBitRate(file.getBitRate());

			dto.setName(file.getName());
			dto.setArtist(file.getArtist());
			dto.setAlbumArtist(file.getAlbumArtist());
			dto.setAlbum(file.getAlbum());
			dto.setYear(file.getYear());

			dto.setDiscNumber(file.getDiscNumber());
			dto.setDiscCount(file.getDiscCount());

			dto.setTrackNumber(file.getTrackNumber());
			dto.setTrackCount(file.getTrackCount());

			dto.setArtwork(file.getArtwork() != null ? file.getArtwork().getId() : null);
			dto.setArtworkUrl(getStoredFileUrl(dto.getArtwork()));
		}

		return dto;
	}

	private void initAlbumDto(AlbumDto aDto, Album aAlbum) {

		aDto.setId(aAlbum.getId());
		aDto.setCreationDate(aAlbum.getCreationDate());
		aDto.setUpdateDate(aAlbum.getUpdateDate());
		aDto.setGeneration(aAlbum.getVersion());

		aDto.setName(aAlbum.getName());
		aDto.setYear(aAlbum.getYear());
		aDto.setArtwork(aAlbum.getArtwork() != null ? aAlbum.getArtwork().getId() : null);
		aDto.setArtworkUrl(getStoredFileUrl(aDto.getArtwork()));

		Artist artist = aAlbum.getArtist();

		if (artist != null) {
			aDto.setArtistId(artist.getId());
			aDto.setArtistName(artist.getName());
			aDto.setArtistArtwork(artist.getArtwork() != null ? artist.getArtwork().getId() : null);
			aDto.setArtistArtworkUrl(getStoredFileUrl(aDto.getArtistArtwork()));
		}
	}

	private String getStoredFileUrl(Long aId) {
		return getApiRelatedUrl(aId, "storedFile");
	}

	private String getSongFileUrl(Long aId) {
		return getApiRelatedUrl(aId, "songFile");
	}

	private String getApiRelatedUrl(Long aId, String aCall) {

		String url = null;

		if (aId != null) {

			HttpServletRequest request = getCurrentRequest();

			if (request != null) {

				StringBuilder buf = new StringBuilder();

				buf.append(request.getScheme()).append("://");
				buf.append(request.getServerName()).append(":").append(request.getServerPort());
				buf.append(request.getContextPath());
				buf.append("/api/").append(aCall).append("/").append(aId);

				url = buf.toString();
			}
		}

		return url;
	}

	private HttpServletRequest getCurrentRequest() {

		HttpServletRequest request = null;

		try {

			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

			request = attributes.getRequest();

		} catch (Throwable e) {
			log.warn("could not get current request", e);
		}

		return request;
	}

}
