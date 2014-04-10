package net.dorokhov.pony.web.client.common;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class AbstractEvent<T extends EventHandler> extends GwtEvent<T> {

	private Type<T> associatedType;

	protected AbstractEvent(Type<T> aAssociatedType) {
		associatedType = aAssociatedType;
	}

	@Override
	public Type<T> getAssociatedType() {
		return associatedType;
	}
}
