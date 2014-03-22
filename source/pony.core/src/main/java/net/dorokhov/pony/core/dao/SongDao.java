package net.dorokhov.pony.core.dao;

import net.dorokhov.pony.core.domain.Song;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SongDao extends PagingAndSortingRepository<Song, Integer> {

	public long countByAlbumId(Integer aAlbumId);

	public long countByAlbumArtistId(Integer aArtistId);

	public List<Song> findByAlbumId(Integer aAlbumId, Sort aSort);

	public List<Song> findByAlbumArtistId(Integer aArtistId, Sort aSort);

	public Song findByFileId(Integer aSongFileId);

	@Transactional
	@Modifying
	@Query("DELETE FROM Song s WHERE s.file.id = ?1")
	public void deleteByFileId(Integer aSongFileId);

}
