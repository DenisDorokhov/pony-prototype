package net.dorokhov.pony.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import net.dorokhov.pony.web.client.common.AbstractEvent;

public class NoDataEvent extends AbstractEvent<NoDataEvent.Handler> {

	public static interface Handler extends EventHandler {
		public void onNoDataEvent(NoDataEvent aEvent);
	}

	public static final GwtEvent.Type<Handler> NO_DATA_DETECTED = new GwtEvent.Type<Handler>();

	public NoDataEvent(Type<NoDataEvent.Handler> aAssociatedType) {
		super(aAssociatedType);
	}

	@Override
	protected void dispatch(NoDataEvent.Handler aHandler) {
		aHandler.onNoDataEvent(this);
	}
}
