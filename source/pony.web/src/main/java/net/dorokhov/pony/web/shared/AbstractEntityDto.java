package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public abstract class AbstractEntityDto<T> implements Serializable {

	private T id;

	public T getId() {
		return id;
	}

	public void setId(T aId) {
		id = aId;
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}

	@Override
	public boolean equals(Object aObj) {

		if (this == aObj) {
			return true;
		}

		if (aObj != null && getId() != null && getClass().equals(aObj.getClass())) {

			AbstractEntityDto entity = (AbstractEntityDto)aObj;

			return getId().equals(entity.getId());
		}

		return false;
	}
}
