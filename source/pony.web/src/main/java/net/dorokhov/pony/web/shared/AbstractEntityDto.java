package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public abstract class AbstractEntityDto implements Serializable {

	private Long id;

	private Long generation;

	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	public Long getGeneration() {
		return generation;
	}

	public void setGeneration(Long aGeneration) {
		generation = aGeneration;
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
