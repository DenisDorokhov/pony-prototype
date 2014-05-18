package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistEvent extends AbstractEvent<ArtistEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onArtistEvent(ArtistEvent aEvent);
	}

	public static final Type<Handler> ARTIST_SELECTED = new Type<Handler>();
	public static final Type<Handler> ARTIST_UPDATED = new Type<Handler>();

	private final Map<Long, ArtistDto> idToArtist = new HashMap<Long, ArtistDto>();

	private final List<ArtistDto> artists;

	public ArtistEvent(Type<Handler> aAssociatedType, ArtistDto aArtist) {

		super(aAssociatedType);

		List<ArtistDto> artistsToUpdate = new ArrayList<ArtistDto>();

		artistsToUpdate.add(aArtist);

		artists = artistsToUpdate;

		updateArtists();
	}

	public ArtistEvent(Type<Handler> aAssociatedType, List<ArtistDto> aArtists) {

		super(aAssociatedType);

		artists = aArtists != null ? aArtists : new ArrayList<ArtistDto>();

		updateArtists();
	}

	public List<ArtistDto> getArtists() {
		return artists;
	}

	public ArtistDto getFirstArtist() {
		return artists.size() > 0 ? artists.get(0) : null;
	}

	public ArtistDto getArtist(Long aId) {
		return idToArtist.get(aId);
	}

	public boolean hasArtist(ArtistDto aArtist) {
		return getArtist(aArtist.getId()) != null;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onArtistEvent(this);
	}

	private void updateArtists() {
		for (ArtistDto artist : getArtists()) {
			idToArtist.put(artist.getId(), artist);
		}
	}
}
