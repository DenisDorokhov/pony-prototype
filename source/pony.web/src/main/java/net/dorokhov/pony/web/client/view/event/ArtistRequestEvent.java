package net.dorokhov.pony.web.client.view.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.ArtistDto;

public class ArtistRequestEvent extends AbstractEvent<ArtistRequestEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onArtistRequest(ArtistRequestEvent aEvent);
	}

	public static interface HasHandler {
		public HandlerRegistration addArtistSelectionRequestHandler(ArtistRequestEvent.Handler aHandler);
	}

	public static final Type<Handler> ARTIST_SELECTION_REQUESTED = new Type<Handler>();

	private ArtistDto artist;

	public ArtistRequestEvent(Type<Handler> aAssociatedType, ArtistDto aArtist) {

		super(aAssociatedType);

		artist = aArtist;
	}

	public ArtistDto getArtist() {
		return artist;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onArtistRequest(this);
	}
}
