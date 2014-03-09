package net.dorokhov.pony.web.service.impl;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.AlbumService;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.web.domain.AlbumDto;
import net.dorokhov.pony.web.service.AlbumServiceRemote;
import net.dorokhov.pony.web.utility.DtoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumServiceRemoteImpl implements AlbumServiceRemote {

	private AlbumService albumService;

	private SongService songService;

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

		List<AlbumDto> albumList = new ArrayList<AlbumDto>();

		for (Album album : albumService.getByArtist(aArtistId)) {
			albumList.add(albumToDto(album));
		}

		return albumList;
	}

	@Override
	public AlbumDto getById(Integer aId) {

		Album album = albumService.getById(aId);

		return album != null ? albumToDto(album) : null;
	}

	private AlbumDto albumToDto(Album aAlbum) {

		AlbumDto dto = DtoUtility.albumToDto(aAlbum);

		for (Song song : songService.getByAlbum(aAlbum.getId())) {
			dto.getSongs().add(DtoUtility.songToDto(song));
		}

		return dto;
	}
}
