package net.dorokhov.pony.core.entity.common;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntityIdentified extends BaseEntityVersioned {

	private Integer id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer aId) {
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
			
			BaseEntityIdentified entity = (BaseEntityIdentified)aObj;
			
			return getId().equals(entity.getId());
		}
		
		return false;
	}
}
