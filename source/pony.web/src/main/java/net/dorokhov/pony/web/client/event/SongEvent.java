package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongEvent extends AbstractEvent<SongEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onSongEvent(SongEvent aEvent);
	}

	public static final Type<Handler> SELECTION = new Type<Handler>();

	public static final Type<Handler> PLAYBACK_REQUESTED = new Type<Handler>();
	public static final Type<Handler> PLAYBACK_STARTED = new Type<Handler>();
	public static final Type<Handler> PLAYBACK_PAUSED = new Type<Handler>();
	public static final Type<Handler> PLAYBACK_ENDED = new Type<Handler>();

	private SongDto song;

	public SongEvent(Type<Handler> aAssociatedType, SongDto aSong) {

		super(aAssociatedType);

		song = aSong;
	}

	public SongDto getSong() {
		return song;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onSongEvent(this);
	}
}
