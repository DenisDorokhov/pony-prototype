package net.dorokhov.pony.web.service.impl;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.AlbumService;
import net.dorokhov.pony.core.service.ArtistService;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.web.domain.AlbumDto;
import net.dorokhov.pony.web.service.AlbumServiceRemote;
import net.dorokhov.pony.web.utility.DtoUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumServiceRemoteImpl implements AlbumServiceRemote {

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
	public Long getCountByArtist(Integer aArtistId) {
		return albumService.getCountByArtist(aArtistId);
	}

	@Override
	public List<AlbumDto> getByArtist(Integer aArtistId) {
		return albumListToDto(albumService.getByArtist(aArtistId));
	}

	@Override
	public List<AlbumDto> getByArtistIdOrName(String aIdOrName) {

		Artist artist = null;

		if (StringUtils.isNumeric(aIdOrName)) {
			artist = artistService.getById(NumberUtils.toInt(aIdOrName));
		}

		if (artist == null) {
			artist = artistService.getByName(aIdOrName);
		}

		return artist != null ? albumListToDto(albumService.getByArtist(artist.getId())) : new ArrayList<AlbumDto>();
	}

	@Override
	public AlbumDto getById(Integer aId) {

		Album album = albumService.getById(aId);

		return album != null ? albumToDto(album) : null;
	}

	private List<AlbumDto> albumListToDto(List<Album> aAlbumList) {

		List<AlbumDto> dto = new ArrayList<AlbumDto>();

		for (Album album : aAlbumList) {
			dto.add(albumToDto(album));
		}

		return dto;
	}

	private AlbumDto albumToDto(Album aAlbum) {

		AlbumDto dto = DtoUtility.albumToDto(aAlbum);

		for (Song song : songService.getByAlbum(aAlbum.getId())) {
			dto.getSongs().add(DtoUtility.songToDto(song));
		}

		return dto;
	}
}