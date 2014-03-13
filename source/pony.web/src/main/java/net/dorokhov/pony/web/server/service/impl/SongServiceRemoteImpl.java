package net.dorokhov.pony.web.server.service.impl;

import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.SongService;
import net.dorokhov.pony.web.server.service.SongServiceRemote;
import net.dorokhov.pony.web.shared.SongDto;
import net.dorokhov.pony.web.server.utility.DtoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SongServiceRemoteImpl implements SongServiceRemote {

	private SongService songService;

	@Autowired
	public void setSongService(SongService aSongService) {
		songService = aSongService;
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
	public List<SongDto> getByAlbum(Integer aAlbumId) {
		return songListToDto(songService.getByAlbum(aAlbumId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<SongDto> getByArtist(Integer aArtistId) {
		return songListToDto(songService.getByArtist(aArtistId));
	}

	@Override
	@Transactional(readOnly = true)
	public SongDto getById(Integer aId) {

		Song song = songService.getById(aId);

		return song != null ? DtoUtility.songToDto(song) : null;
	}

	private List<SongDto> songListToDto(List<Song> aSongList) {

		List<SongDto> songList = new ArrayList<SongDto>();

		for (Song song : aSongList) {
			songList.add(DtoUtility.songToDto(song));
		}

		return songList;
	}
}
