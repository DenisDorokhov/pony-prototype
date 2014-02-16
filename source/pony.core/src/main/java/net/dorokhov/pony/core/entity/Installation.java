package net.dorokhov.pony.core.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "installation")
public class Installation extends BaseEntityIdentified implements Serializable {

	private static final long serialVersionUID = 1L;

	private String version;

	@Column(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String aVersion) {
		version = aVersion;
	}

}
