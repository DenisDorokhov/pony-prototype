package net.dorokhov.pony.web.client.view;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.IsWidget;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.List;

public interface ArtistsView extends IsWidget {

	public EventBus getEventBus();

	public void setEventBus(EventBus aEventBus);

	public ContentState getArtistsContentState();

	public void setArtistsContentState(ContentState aContentState);

	public ContentState getAlbumsContentState();

	public void setAlbumsContentState(ContentState aContentState);

	public List<ArtistDto> getArtists();

	public void setArtists(List<ArtistDto> aArtists);

	public ArtistDto getSelectedArtist();

	public void setSelectedArtist(ArtistDto aArtist);

	public List<AlbumSongsDto> getAlbums();

	public void setAlbums(List<AlbumSongsDto> aAlbums);

}
