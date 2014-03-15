package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class ArtistDto extends AbstractEntityDto implements Serializable {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}
}
