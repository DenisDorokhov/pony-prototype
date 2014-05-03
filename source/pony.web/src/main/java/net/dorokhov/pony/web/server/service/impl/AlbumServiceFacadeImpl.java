package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.AlbumService;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.web.server.service.AlbumServiceFacade;
import net.dorokhov.pony.web.server.service.DtoService;
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

	private DtoService dtoService;

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

	@Autowired
	public void setDtoService(DtoService aDtoService) {
		dtoService = aDtoService;
	}

	@Override
	public Long getCountByArtist(Long aArtistId) {
		return albumService.getCountByArtist(aArtistId);
	}

	@Override
	public ArrayList<AlbumSongsDto> getByArtist(Long aArtistId) {
		return songListToDto(songService.getByArtist(aArtistId));
	}

	@Override
	public ArrayList<AlbumSongsDto> getByArtistIdOrName(String aIdOrName) {

		Artist artist = null;

		if (StringUtils.isNumeric(aIdOrName)) {
			artist = artistService.getById(NumberUtils.toLong(aIdOrName));
		}

		if (artist == null) {
			artist = artistService.getByName(aIdOrName);
		}

		return artist != null ? songListToDto(songService.getByArtist(artist.getId())) : new ArrayList<AlbumSongsDto>();
	}

	@Override
	public ArrayList<AlbumDto> search(String aQuery) {
		return albumListToDto(albumService.search(aQuery));
	}

	@Override
	public AlbumSongsDto getById(Long aId) {

		List<AlbumSongsDto> dto = songListToDto(songService.getByAlbum(aId));

		return dto.size() > 0 ? dto.get(0) : dtoService.albumToSongsDto(albumService.getById(aId), new ArrayList<Song>());
	}

	private ArrayList<AlbumSongsDto> songListToDto(List<Song> aSongList) {

		ArrayList<AlbumSongsDto> result = new ArrayList<AlbumSongsDto>();

		List<Song> emptySongList = new ArrayList<Song>();

		AlbumSongsDto currentDto = null;

		for (Song song : aSongList) {

			if (currentDto == null || !currentDto.getId().equals(song.getAlbum().getId())) {

				currentDto = dtoService.albumToSongsDto(song.getAlbum(), emptySongList);

				result.add(currentDto);
			}

			currentDto.getSongs().add(dtoService.songToDto(song));
		}

		return result;
	}

	private ArrayList<AlbumDto> albumListToDto(List<Album> aAlbumList) {

		ArrayList<AlbumDto> dto = new ArrayList<AlbumDto>();

		for (Album album : aAlbumList) {
			dto.add(dtoService.albumToDto(album));
		}

		return dto;
	}
}
