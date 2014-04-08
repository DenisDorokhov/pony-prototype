package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.web.server.service.DtoService;
import net.dorokhov.pony.web.server.service.SongServiceFacade;
import net.dorokhov.pony.web.shared.SongDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SongServiceFacadeImpl implements SongServiceFacade {

	private SongService songService;

	private DtoService dtoService;

	@Autowired
	public void setSongService(SongService aSongService) {
		songService = aSongService;
	}

	@Autowired
	public void setDtoService(DtoService aDtoService) {
		dtoService = aDtoService;
	}

	@Override
	@Transactional(readOnly = true)
	public Long getCountByAlbum(Integer aAlbumId) {
		return songService.getCountByAlbum(aAlbumId);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getCountByArtist(Integer aArtistId) {
		return songService.getCountByArtist(aArtistId);
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<SongDto> getByAlbum(Integer aAlbumId) {
		return songListToDto(songService.getByAlbum(aAlbumId));
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<SongDto> getByArtist(Integer aArtistId) {
		return songListToDto(songService.getByArtist(aArtistId));
	}

	@Override
	@Transactional(readOnly = true)
	public ArrayList<SongDto> search(String aQuery) {
		return songListToDto(songService.search(aQuery));
	}

	@Override
	@Transactional(readOnly = true)
	public SongDto getById(Integer aId) {

		Song song = songService.getById(aId);

		return song != null ? dtoService.songToDto(song) : null;
	}

	private ArrayList<SongDto> songListToDto(List<Song> aSongList) {

		ArrayList<SongDto> songList = new ArrayList<SongDto>();

		for (Song song : aSongList) {
			songList.add(dtoService.songToDto(song));
		}

		return songList;
	}
}
