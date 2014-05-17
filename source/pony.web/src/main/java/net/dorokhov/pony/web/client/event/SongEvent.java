package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongEvent extends AbstractEvent<SongEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onSongEvent(SongEvent aEvent);
	}

	public static final Type<Handler> SONG_SELECTED = new Type<Handler>();

	public static final Type<Handler> SONG_STARTED = new Type<Handler>();
	public static final Type<Handler> SONG_PAUSED = new Type<Handler>();
	public static final Type<Handler> SONG_ENDED = new Type<Handler>();
	public static final Type<Handler> SONG_FAILED = new Type<Handler>();

	private final SongDto song;

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
