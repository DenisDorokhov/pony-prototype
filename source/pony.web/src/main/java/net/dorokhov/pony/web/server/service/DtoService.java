package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.core.domain.Album;
import net.dorokhov.pony.core.domain.Artist;
import net.dorokhov.pony.core.domain.Configuration;
import net.dorokhov.pony.core.domain.Song;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.shared.*;

import java.util.List;

public interface DtoService {

	public ConfigurationDto configurationToDto(Configuration aConfig);

	public Configuration dtoToConfiguration(ConfigurationDto aDto);

	public StatusDto statusToDto(LibraryScanner.Status aStatus);

	public ArtistDto artistToDto(Artist aArtist);

	public AlbumDto albumToDto(Album aAlbum);

	public AlbumSongsDto albumToSongsDto(Album aAlbum, List<Song> aSongs);

	public SongDto songToDto(Song aSong);

}
