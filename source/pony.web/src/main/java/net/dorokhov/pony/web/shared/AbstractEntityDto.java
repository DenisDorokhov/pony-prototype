package net.dorokhov.pony.web.shared;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractEntityDto implements Serializable {

	private Integer id;

	private Date creationDate;

	private Date updateDate;

	private Long generation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer aId) {
		id = aId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date aCreationDate) {
		creationDate = aCreationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date aUpdateDate) {
		updateDate = aUpdateDate;
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
