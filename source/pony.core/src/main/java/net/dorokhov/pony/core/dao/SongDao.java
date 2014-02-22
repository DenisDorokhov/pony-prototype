package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.entity.Song;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SongDao extends PagingAndSortingRepository<Song, Integer> {

	public long countByAlbumId(Integer aAlbumId);

	public long countByAlbumArtistId(Integer aArtistId);

	public List<Song> findByAlbumId(Integer aAlbumId);

	public List<Song> findByAlbumArtistId(Integer aArtistId);

	public Song findByFileId(Integer aSongFileId);

}
