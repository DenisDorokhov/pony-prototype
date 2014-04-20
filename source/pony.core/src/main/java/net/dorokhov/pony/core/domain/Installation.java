package net.dorokhov.pony.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * System installation.
 *
 * Installation currently have only one special property: version. It is used to manage automatic system updates.
 */
@Entity
@Table(name = "installation")
public class Installation extends AbstractEntity<Long> {

	private String systemVersion;

	@Column(name = "system_version")
	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String aVersion) {
		systemVersion = aVersion;
	}

	@Override
	public String toString() {
		return "Installation{" +
				"id=" + getId() +
				", version='" + systemVersion + '\'' +
				'}';
	}
}
