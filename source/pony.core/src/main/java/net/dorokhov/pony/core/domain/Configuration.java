package net.dorokhov.pony.core.domain;

import net.dorokhov.pony.core.dao.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Configuration entity.
 *
 * This entity is used to store system settings.
 */
@Entity
@Table(name = "configuration")
public class Configuration implements AbstractEntity<String> {

	private String id;

	private Date creationDate;

	private Date updateDate;

	private Long version;

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

	@Override
	@Id
	@Column(name = "id")
	@NotNull
	public String getId() {
		return id;
	}

	@Override
	public void setId(String aId) {
		id = aId;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}

	@Override
	public void setCreationDate(Date aCreationDate) {
		creationDate = aCreationDate;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public void setUpdateDate(Date aUpdateDate) {
		updateDate = aUpdateDate;
	}

	@Override
	@Version
	@Column(name = "version")
	public Long getVersion() {
		return version;
	}

	@Override
	public void setVersion(Long aVersion) {
		version = aVersion;
	}

	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String aValue) {
		value = aValue;
	}

	@Transient
	public long getLong() {
		return Long.valueOf(value);
	}

	public void setLong(long aValue) {
		value = String.valueOf(aValue);
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

	@Override
	public String toString() {
		return "Configuration{" +
				"id=" + getId() +
				", value='" + value + '\'' +
				'}';
	}
}
