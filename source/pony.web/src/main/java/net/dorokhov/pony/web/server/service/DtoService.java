package net.dorokhov.pony.web.server.service;

import net.dorokhov.pony.core.domain.*;
import net.dorokhov.pony.core.service.LibraryScanner;
import net.dorokhov.pony.web.shared.*;

import java.util.List;

public interface DtoService {

	public InstallationDto installationToDto(Installation aInstallation);

	public ConfigurationDto configurationToDto(Configuration aConfig);

	public Configuration dtoToConfiguration(ConfigurationDto aDto);

	public StatusDto statusToDto(LibraryScanner.Status aStatus);

	public ArtistDto artistToDto(Artist aArtist);

	public AlbumDto albumToDto(Album aAlbum);

	public AlbumSongsDto albumToSongsDto(Album aAlbum, List<Song> aSongs);

	public SongDto songToDto(Song aSong);

}
