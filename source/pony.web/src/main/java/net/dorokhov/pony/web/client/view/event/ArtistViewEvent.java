package net.dorokhov.pony.web.client.view.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.ArtistDto;

public class ArtistViewEvent extends AbstractEvent<ArtistViewEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onArtistViewEvent(ArtistViewEvent aEvent);
	}

	public static interface HasHandler {
		public HandlerRegistration addArtistSelectionRequestHandler(ArtistViewEvent.Handler aHandler);
	}

	public static final Type<Handler> ARTIST_SELECTION_REQUESTED = new Type<Handler>();

	private ArtistDto artist;

	public ArtistViewEvent(Type<Handler> aAssociatedType, ArtistDto aArtist) {

		super(aAssociatedType);

		artist = aArtist;
	}

	public ArtistDto getArtist() {
		return artist;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onArtistViewEvent(this);
	}
}
