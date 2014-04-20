package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.client.service.PlayList;

public class PlayListEvent extends AbstractEvent<PlayListEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onPlayListEvent(PlayListEvent aEvent);
	}

	public static final Type<Handler> PLAYBACK_REQUESTED = new Type<Handler>();

	private PlayList playList;

	public PlayListEvent(Type<Handler> aAssociatedType, PlayList aPlayList) {

		super(aAssociatedType);

		playList = aPlayList;
	}

	public PlayList getPlayList() {
		return playList;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onPlayListEvent(this);
	}

}
