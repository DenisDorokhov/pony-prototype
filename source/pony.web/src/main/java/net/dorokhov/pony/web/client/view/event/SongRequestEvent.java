package net.dorokhov.pony.web.client.view.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongRequestEvent extends AbstractEvent<SongRequestEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onSongRequest(SongRequestEvent aEvent);
	}

	public static interface HasHandler {
		public HandlerRegistration addSongSelectionRequestHandler(SongRequestEvent.Handler aHandler);
		public HandlerRegistration addSongActivationRequestHandler(SongRequestEvent.Handler aHandler);
	}

	public static final Type<Handler> SONG_SELECTION_REQUESTED = new Type<Handler>();
	public static final Type<Handler> SONG_ACTIVATION_REQUESTED = new Type<Handler>();

	private SongDto song;

	public SongRequestEvent(Type<Handler> aAssociatedType, SongDto aSong) {

		super(aAssociatedType);

		song = aSong;
	}

	public SongDto getSong() {
		return song;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onSongRequest(this);
	}
}
