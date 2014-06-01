package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class InstallationDto extends AbstractEntityDto<Long> implements Serializable {

	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String aVersion) {
		version = aVersion;
	}

	@Override
	public String toString() {
		return "InstallationDto{" +
				"version='" + version + '\'' +
				'}';
	}
}
