package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.*;
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

	@Override
	public InstallationDto installationToDto(Installation aInstallation) {

		InstallationDto dto = new InstallationDto();

		dto.setId(aInstallation.getId());
		dto.setVersion(aInstallation.getVersion());
		dto.setSystemVersion(aInstallation.getSystemVersion());

		return dto;
	}

	@Override
	public ConfigurationDto configurationToDto(Configuration aConfig) {

		ConfigurationDto dto = new ConfigurationDto();

		dto.setId(aConfig.getId());
		dto.setVersion(aConfig.getVersion());
		dto.setValue(aConfig.getValue());

		return dto;
	}

	@Override
	public Configuration dtoToConfiguration(ConfigurationDto aDto) {

		Configuration config = new Configuration();

		config.setId(aDto.getId());
		config.setVersion(aDto.getVersion());
		config.setValue(aDto.getValue());

		return config;
	}

	@Override
	public ScanResultDto scanResultToDto(ScanResult aScanResult) {

		ScanResultDto dto = new ScanResultDto();

		dto.setId(aScanResult.getId());
		dto.setVersion(aScanResult.getVersion());
		dto.setDate(aScanResult.getDate());
		dto.setSuccess(aScanResult.getSuccess());
		dto.setTargetFiles(aScanResult.getTargetFiles());
		dto.setDuration(aScanResult.getDuration());
		dto.setScannedFolderCount(aScanResult.getScannedFolderCount());
		dto.setScannedFileCount(aScanResult.getScannedFileCount());
		dto.setImportedFileCount(aScanResult.getImportedFileCount());

		return dto;
	}

	@Override
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

	@Override
	public ArtistDto artistToDto(Artist aArtist) {

		ArtistDto dto = new ArtistDto();

		dto.setId(aArtist.getId());
		dto.setVersion(aArtist.getVersion());

		dto.setName(aArtist.getName());
		dto.setArtwork(aArtist.getArtwork() != null ? aArtist.getArtwork().getId() : null);
		dto.setArtworkUrl(getStoredFileUrl(dto.getArtwork()));

		return dto;
	}

	@Override
	public AlbumDto albumToDto(Album aAlbum) {

		AlbumDto dto = new AlbumDto();

		initAlbumDto(dto, aAlbum);

		return dto;
	}

	@Override
	public AlbumSongsDto albumToSongsDto(Album aAlbum, List<Song> aSongs) {

		AlbumSongsDto dto = new AlbumSongsDto();

		initAlbumDto(dto, aAlbum);

		for (Song song : aSongs) {
			dto.getSongs().add(songToDto(song));
		}

		return dto;
	}

	@Override
	public SongDto songToDto(Song aSong) {

		SongDto dto = new SongDto();

		dto.setId(aSong.getId());
		dto.setVersion(aSong.getVersion());

		Album album = aSong.getAlbum();

		if (album != null) {

			dto.setAlbumId(album.getId());
			dto.setAlbumName(album.getName());
			dto.setAlbumArtwork(album.getArtwork() != null ? album.getArtwork().getId() : null);
			dto.setAlbumArtworkUrl(getStoredFileUrl(album.getArtwork() != null ? album.getArtwork().getId() : null));
			dto.setAlbumYear(album.getYear());

			Artist artist = album.getArtist();

			if (artist != null) {
				dto.setArtistId(artist.getId());
				dto.setArtistName(artist.getName());
			}
		}

		SongFile file = aSong.getFile();

		if (file != null) {

			dto.setFileUrl(getSongFileUrl(file.getId()));

			dto.setDuration(file.getDuration());

			dto.setName(file.getName());
			dto.setArtist(file.getArtist());

			dto.setDiscNumber(file.getDiscNumber());
			dto.setTrackNumber(file.getTrackNumber());
		}

		return dto;
	}

	private void initAlbumDto(AlbumDto aDto, Album aAlbum) {

		aDto.setId(aAlbum.getId());
		aDto.setVersion(aAlbum.getVersion());

		aDto.setName(aAlbum.getName());
		aDto.setYear(aAlbum.getYear());
		aDto.setArtwork(aAlbum.getArtwork() != null ? aAlbum.getArtwork().getId() : null);
		aDto.setArtworkUrl(getStoredFileUrl(aAlbum.getArtwork() != null ? aAlbum.getArtwork().getId() : null));

		Artist artist = aAlbum.getArtist();

		if (artist != null) {
			aDto.setArtistId(artist.getId());
			aDto.setArtistName(artist.getName());
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

		} catch (Exception e) {
			log.warn("could not get current request", e);
		}

		return request;
	}

}
