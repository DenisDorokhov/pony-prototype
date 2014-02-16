package net.dorokhov.pony.core.entity;

import net.dorokhov.pony.core.entity.common.BaseEntityIdentified;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sound_file")
public class AudioFile extends BaseEntityIdentified {

	private String path;

	private String type;

	private Long size;

	@Column(name = "name")
	@NotBlank
	public String getPath() {
		return path;
	}

	public void setPath(String aPath) {
		path = aPath;
	}

	@Column(name = "type")
	@NotBlank
	@Size(max = 255)
	public String getType() {
		return type;
	}

	public void setType(String aType) {
		type = aType;
	}

	@Column(name = "size")
	public Long getSize() {
		return size;
	}

	public void setSize(Long aSize) {
		size = aSize;
	}
}
