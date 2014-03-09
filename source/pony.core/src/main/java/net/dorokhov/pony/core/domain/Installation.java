package net.dorokhov.pony.core.domain;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "installation")
public class Installation extends AbstractEntityIdentified implements Serializable {

	private String version;

	@Column(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String aVersion) {
		version = aVersion;
	}
}
