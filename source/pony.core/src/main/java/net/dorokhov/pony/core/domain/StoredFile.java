package net.dorokhov.pony.core.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stored_file")
public class StoredFile extends AbstractEntity<Integer> {

	private String name;

	private String mimeType;

	private String checksum;

	private String path;

	@Column(name = "name")
	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	@Column(name = "mime_type")
	@NotBlank
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String aMimeType) {
		mimeType = aMimeType;
	}

	@Column(name = "checksum")
	@NotBlank
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String aChecksum) {
		checksum = aChecksum;
	}

	@Column(name = "relative_path", unique = true)
	@NotBlank
	public String getPath() {
		return path;
	}

	public void setPath(String aPath) {
		path = aPath;
	}
}
