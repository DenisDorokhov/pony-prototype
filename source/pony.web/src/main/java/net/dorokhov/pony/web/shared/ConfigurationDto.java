package net.dorokhov.pony.web.shared;

public class ConfigurationDto extends AbstractEntityDto<String> {

	private String value;

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
