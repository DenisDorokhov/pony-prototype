package net.dorokhov.pony.web.server.utility;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.domain.SongFile;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.shared.*;

import java.io.File;

public class DtoConverter {

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

		initAlbumDto(dto, aAlbum);

		return dto;
	}

	public static AlbumSongsDto albumToSongsDto(Album aAlbum) {

		AlbumSongsDto dto = new AlbumSongsDto();

		initAlbumDto(dto, aAlbum);

		return dto;
	}

	private static void initAlbumDto(AlbumDto aDto, Album aAlbum) {

		aDto.setId(aAlbum.getId());
		aDto.setCreationDate(aAlbum.getCreationDate());
		aDto.setUpdateDate(aAlbum.getUpdateDate());
		aDto.setGeneration(aAlbum.getGeneration());

		aDto.setName(aAlbum.getName());
		aDto.setYear(aAlbum.getYear());
		aDto.setArtwork(aAlbum.getArtwork() != null ? aAlbum.getArtwork().getId() : null);

		Artist artist = aAlbum.getArtist();

		if (artist != null) {
			aDto.setArtistId(artist.getId());
			aDto.setArtistName(artist.getName());
			aDto.setArtistArtwork(artist.getArtwork() != null ? artist.getArtwork().getId() : null);
		}
	}

	public static SongDto songToDto(Song aSong) {

		SongDto dto = new SongDto();

		dto.setId(aSong.getId());
		dto.setCreationDate(aSong.getCreationDate());
		dto.setUpdateDate(aSong.getUpdateDate());
		dto.setGeneration(aSong.getGeneration());

		Album album = aSong.getAlbum();

		if (album != null) {

			dto.setAlbumId(album.getId());
			dto.setAlbumName(album.getName());
			dto.setAlbumArtwork(album.getArtwork() != null ? album.getArtwork().getId() : null);
			dto.setAlbumYear(album.getYear());

			Artist artist = album.getArtist();

			if (artist != null) {
				dto.setArtistId(artist.getId());
				dto.setArtistName(artist.getName());
				dto.setArtistArtwork(artist.getArtwork() != null ? artist.getArtwork().getId() : null);
			}
		}

		SongFile file = aSong.getFile();

		if (file != null) {

			dto.setFile(file.getId());
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
		}

		return dto;
	}
}
