package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;
import net.dorokhov.pony.web.client.service.PlayList;

public class PlayListEvent extends AbstractEvent<PlayListEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onPlayListEvent(PlayListEvent aEvent);
	}

	public static final Type<Handler> PLAYLIST_CHANGE = new Type<Handler>();

	private final PlayList playList;

	private final int startIndex;

	public PlayListEvent(Type<Handler> aAssociatedType, PlayList aPlayList, int aStartIndex) {

		super(aAssociatedType);

		playList = aPlayList;
		startIndex = aStartIndex;
	}

	public PlayList getPlayList() {
		return playList;
	}

	public int getStartIndex() {
		return startIndex;
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onPlayListEvent(this);
	}

}
