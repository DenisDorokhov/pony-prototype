package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import net.dorokhov.pony.web.client.common.AbstractEvent;

public class RefreshEvent extends AbstractEvent<RefreshEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onRefreshEvent(RefreshEvent aEvent);
	}

	public static final Type<Handler> REFRESH_REQUESTED = new Type<Handler>();

	public RefreshEvent(Type<Handler> aAssociatedType) {
		super(aAssociatedType);
	}

	@Override
	protected void dispatch(Handler aHandler) {
		aHandler.onRefreshEvent(this);
	}
}
