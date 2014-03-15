package net.dorokhov.pony.core.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntityIdentified<T extends Serializable> extends AbstractEntityVersioned {

	private T id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
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
			
			AbstractEntityIdentified entity = (AbstractEntityIdentified)aObj;
			
			return getId().equals(entity.getId());
		}
		
		return false;
	}
}
