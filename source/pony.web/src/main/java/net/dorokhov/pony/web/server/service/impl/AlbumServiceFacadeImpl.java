package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.AlbumService;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.web.server.service.AlbumServiceFacade;
import net.dorokhov.pony.web.server.utility.DtoConverter;
import net.dorokhov.pony.web.shared.AlbumDto;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumServiceFacadeImpl implements AlbumServiceFacade {

	private ArtistService artistService;

	private AlbumService albumService;

	private SongService songService;

	@Autowired
	public void setArtistService(ArtistService aArtistService) {
		artistService = aArtistService;
	}

	@Autowired
	public void setAlbumService(AlbumService aAlbumService) {
		albumService = aAlbumService;
	}

	@Autowired
	public void setSongService(SongService aSongService) {
		songService = aSongService;
	}

	@Override
	@Transactional(readOnly = true)
	public Long getCountByArtist(Integer aArtistId) {
		return albumService.getCountByArtist(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<AlbumSongsDto> getByArtist(Integer aArtistId) {
		return songListToDto(songService.getByArtist(aArtistId));
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<AlbumSongsDto> getByArtistIdOrName(String aIdOrName) {

		Artist artist = null;

		if (StringUtils.isNumeric(aIdOrName)) {
			artist = artistService.getById(NumberUtils.toInt(aIdOrName));
		}

		if (artist == null) {
			artist = artistService.getByName(aIdOrName);
		}

		return artist != null ? songListToDto(songService.getByArtist(artist.getId())) : new ArrayList<AlbumSongsDto>();
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<AlbumDto> search(String aQuery) {
		return albumListToDto(albumService.search(aQuery));
	}

	@Override
	@Transactional(readOnly = true)
	public AlbumSongsDto getById(Integer aId) {

		List<AlbumSongsDto> dto = songListToDto(songService.getByAlbum(aId));

		return dto.size() > 0 ? dto.get(0) : DtoConverter.albumToSongsDto(albumService.getById(aId));
	}

	private ArrayList<AlbumSongsDto> songListToDto(List<Song> aSongList) {

		ArrayList<AlbumSongsDto> result = new ArrayList<AlbumSongsDto>();

		AlbumSongsDto currentDto = null;

		for (Song song : aSongList) {

			if (currentDto == null || !currentDto.getId().equals(song.getAlbum().getId())) {

				currentDto = DtoConverter.albumToSongsDto(song.getAlbum());

				result.add(currentDto);
			}

			currentDto.getSongs().add(DtoConverter.songToDto(song));
		}

		return result;
	}

	private ArrayList<AlbumDto> albumListToDto(List<Album> aAlbumList) {

		ArrayList<AlbumDto> dto = new ArrayList<AlbumDto>();

		for (Album album : aAlbumList) {
			dto.add(DtoConverter.albumToDto(album));
		}

		return dto;
	}
}
