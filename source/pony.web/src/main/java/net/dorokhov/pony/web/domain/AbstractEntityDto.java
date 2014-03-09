package net.dorokhov.pony.web.domain;

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
}
