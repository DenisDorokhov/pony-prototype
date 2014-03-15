package net.dorokhov.pony.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "installation")
public class Installation extends AbstractEntity<Integer> {

	private String version;

	@Column(name = "version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String aVersion) {
		version = aVersion;
	}
}
