package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;

public class PlaybackEvent extends AbstractEvent<PlaybackEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onPlaybackEvent(PlaybackEvent aEvent);
	}

	public static final Type<Handler> PLAYBACK_REQUESTED = new Type<Handler>();

	public PlaybackEvent(Type<Handler> aAssociatedType) {
		super(aAssociatedType);
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onPlaybackEvent(this);
	}
}
