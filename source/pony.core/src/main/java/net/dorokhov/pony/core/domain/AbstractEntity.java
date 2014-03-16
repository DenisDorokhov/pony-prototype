package net.dorokhov.pony.core.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> {

	private T id;

	private Date creationDate;

	private Date updateDate;

	private Long generation;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public T getId() {
		return id;
	}

	public void setId(T aId) {
		id = aId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date aCreationDate) {
		creationDate = aCreationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date aUpdateDate) {
		updateDate = aUpdateDate;
	}

	@Version
	@Column(name = "generation")
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
			
			AbstractEntity entity = (AbstractEntity)aObj;
			
			return getId().equals(entity.getId());
		}
		
		return false;
	}
}