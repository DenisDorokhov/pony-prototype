package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class InstallationDto extends AbstractEntityDto<Long> implements Serializable {

	private String systemVersion;

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String aSystemVersion) {
		systemVersion = aSystemVersion;
	}

	@Override
	public String toString() {
		return "InstallationDto{" +
				"systemVersion='" + systemVersion + '\'' +
				'}';
	}
}
