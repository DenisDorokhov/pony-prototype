package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.ArtistDto;

public class ArtistEvent extends AbstractEvent<ArtistEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onArtistEvent(ArtistEvent aEvent);
	}

	public static final Type<Handler> ARTIST_SELECTED = new Type<Handler>();

	private ArtistDto artist;

	public ArtistEvent(Type<Handler> aAssociatedType, ArtistDto aArtist) {

		super(aAssociatedType);

		artist = aArtist;
	}

	public ArtistDto getArtist() {
		return artist;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onArtistEvent(this);
	}
}
