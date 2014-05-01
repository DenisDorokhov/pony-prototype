package net.dorokhov.pony.web.client.view.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongActivationEvent extends AbstractEvent<SongActivationEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onSongActivation(SongActivationEvent aEvent);
	}

	public static interface HasHandler {
		public HandlerRegistration addSongActivationHandler(SongActivationEvent.Handler aHandler);
	}

	public static final Type<Handler> SONG_ACTIVATED = new Type<Handler>();

	private SongDto song;

	public SongActivationEvent(Type<Handler> aAssociatedType, SongDto aSong) {

		super(aAssociatedType);

		song = aSong;
	}

	public SongDto getSong() {
		return song;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onSongActivation(this);
	}
}
