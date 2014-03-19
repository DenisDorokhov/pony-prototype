package net.dorokhov.pony.web.server.utility;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;
import net.dorokhov.pony.web.shared.StatusDto;

import java.io.File;

public class DtoUtility {

	public static StatusDto statusToDto(LibraryScanner.Status aStatus) {

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

	public static ArtistDto artistToDto(Artist aArtist) {

		ArtistDto dto = new ArtistDto();

		dto.setId(aArtist.getId());
		dto.setCreationDate(aArtist.getCreationDate());
		dto.setUpdateDate(aArtist.getUpdateDate());
		dto.setGeneration(aArtist.getGeneration());

		dto.setName(aArtist.getName());
		dto.setArtwork(aArtist.getArtwork() != null ? aArtist.getArtwork().getId() : null);

		return dto;
	}

	public static AlbumDto albumToDto(Album aAlbum) {

		AlbumDto dto = new AlbumDto();

		dto.setId(aAlbum.getId());
		dto.setCreationDate(aAlbum.getCreationDate());
		dto.setUpdateDate(aAlbum.getUpdateDate());
		dto.setGeneration(aAlbum.getGeneration());

		dto.setName(aAlbum.getName());
		dto.setYear(aAlbum.getYear());
		dto.setDiscCount(aAlbum.getDiscCount());
		dto.setTrackCount(aAlbum.getTrackCount());
		dto.setArtwork(aAlbum.getArtwork() != null ? aAlbum.getArtwork().getId() : null);

		if (aAlbum.getArtist() != null) {
			dto.setArtistId(aAlbum.getArtist().getId());
			dto.setArtist(aAlbum.getArtist().getName());
		}

		return dto;
	}

	public static SongDto songToDto(Song aSong) {

		SongDto dto = new SongDto();

		dto.setId(aSong.getId());
		dto.setCreationDate(aSong.getCreationDate());
		dto.setUpdateDate(aSong.getUpdateDate());
		dto.setGeneration(aSong.getGeneration());

		if (aSong.getAlbum() != null) {

			dto.setAlbumId(aSong.getAlbum().getId());
			dto.setAlbum(aSong.getAlbum().getName());
			dto.setYear(aSong.getAlbum().getYear());

			if (aSong.getAlbum().getArtist() != null) {
				dto.setArtistId(aSong.getAlbum().getArtist().getId());
				dto.setArtist(aSong.getAlbum().getArtist().getName());
			}
		}

		SongFile file = aSong.getFile();

		if (file != null) {

			dto.setPath(file.getPath());
			dto.setFormat(file.getFormat());
			dto.setMimeType(file.getMimeType());
			dto.setSize(file.getSize());
			dto.setDuration(file.getDuration());
			dto.setBitRate(file.getBitRate());

			dto.setName(file.getName());

			dto.setDiscNumber(file.getDiscNumber());
			dto.setDiscCount(file.getDiscCount());

			dto.setTrackNumber(file.getTrackNumber());
			dto.setTrackCount(file.getTrackCount());

			dto.setArtwork(file.getArtwork() != null ? file.getArtwork().getId() : null);
		}

		return dto;
	}
}
