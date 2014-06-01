package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class ConfigurationDto extends AbstractEntityDto<String> implements Serializable {

	private String value;

	public ConfigurationDto() {}

	public ConfigurationDto(String aId, String aValue) {
		setId(aId);
		setValue(aValue);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String aValue) {
		value = aValue;
	}

	@Override
	public String toString() {
		return "ConfigurationDto{" +
				"id='" + getId() + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
