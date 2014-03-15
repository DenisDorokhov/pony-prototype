package net.dorokhov.pony.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "configuration")
public class Configuration extends AbstractEntityIdentified<String> {

	private String value;

	public Configuration() {
		this(null, null);
	}

	public Configuration(String aId, String aValue) {
		setId(aId);
		setValue(aValue);
	}

	public Configuration(String aId, int aValue) {
		this(aId, String.valueOf(aValue));
	}

	public Configuration(String aId, double aValue) {
		this(aId, String.valueOf(aValue));
	}

	public Configuration(String aId, boolean aValue) {
		this(aId, String.valueOf(aValue));
	}

	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String aValue) {
		value = aValue;
	}

	@Transient
	public int getInteger() {
		return Integer.valueOf(value);
	}

	public void setInteger(int aValue) {
		value = String.valueOf(aValue);
	}

	@Transient
	public double getDouble() {
		return Double.valueOf(value);
	}

	public void setDouble(double aValue) {
		value = String.valueOf(aValue);
	}

	@Transient
	public boolean getBoolean() {
		return Boolean.valueOf(value);
	}

	public void setBoolean(boolean aValue) {
		value = Boolean.toString(aValue);
	}

}
