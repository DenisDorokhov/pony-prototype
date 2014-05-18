package net.dorokhov.pony.web.client.view.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongViewEvent extends AbstractEvent<SongViewEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onSongViewEvent(SongViewEvent aEvent);
	}

	public static interface HasHandler {
		public HandlerRegistration addSongSelectionRequestHandler(SongViewEvent.Handler aHandler);
		public HandlerRegistration addSongActivationRequestHandler(SongViewEvent.Handler aHandler);
	}

	public static final Type<Handler> SONG_SELECTION_REQUESTED = new Type<Handler>();
	public static final Type<Handler> SONG_ACTIVATION_REQUESTED = new Type<Handler>();

	private SongDto song;

	public SongViewEvent(Type<Handler> aAssociatedType, SongDto aSong) {

		super(aAssociatedType);

		song = aSong;
	}

	public SongDto getSong() {
		return song;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onSongViewEvent(this);
	}
}
