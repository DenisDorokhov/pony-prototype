package net.dorokhov.pony.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntityVersioned {

	private Date creationDate;

	private Date updateDate;

	private long generation;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date aCreationDate) {
		this.creationDate = aCreationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date aUpdateDate) {
		this.updateDate = aUpdateDate;
	}

	@Version
	@Column(name = "generation")
	public long getGeneration() {
		return generation;
	}

	public void setGeneration(long aGeneration) {
		this.generation = aGeneration;
	}

}
