package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongPlaybackEvent extends AbstractEvent<SongPlaybackEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onSongPlayback(SongPlaybackEvent aEvent);
	}

	public static final Type<Handler> PLAYBACK_REQUESTED = new Type<Handler>();
	public static final Type<Handler> PLAYBACK_STARTED = new Type<Handler>();
	public static final Type<Handler> PLAYBACK_PAUSED = new Type<Handler>();
	public static final Type<Handler> PLAYBACK_RESUMED = new Type<Handler>();

	private SongDto song;

	public SongPlaybackEvent(Type<Handler> aAssociatedType, SongDto aSong) {

		super(aAssociatedType);

		song = aSong;
	}

	public SongDto getSong() {
		return song;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onSongPlayback(this);
	}
}
