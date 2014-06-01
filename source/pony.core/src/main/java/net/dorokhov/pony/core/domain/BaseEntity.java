package net.dorokhov.pony.core.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Abstract entity stored in the database.
 *
 * @param <T> primary key type
 */
@MappedSuperclass
public abstract class BaseEntity<T extends Serializable> implements AbstractEntity<T> {

	private T id;

	private Date creationDate;

	private Date updateDate;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public T getId() {
		return id;
	}

	@Override
	public void setId(T aId) {
		id = aId;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public void setCreationDate(Date aCreationDate) {
		creationDate = aCreationDate;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public void setUpdateDate(Date aUpdateDate) {
		updateDate = aUpdateDate;
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
			
			BaseEntity entity = (BaseEntity)aObj;
			
			return getId().equals(entity.getId());
		}
		
		return false;
	}
}
