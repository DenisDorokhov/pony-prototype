package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class ArtistDto extends AbstractEntityDto implements Serializable {

	private String name;

	private Integer artwork;

	private String artworkUrl;

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	public Integer getArtwork() {
		return artwork;
	}

	public void setArtwork(Integer aArtwork) {
		artwork = aArtwork;
	}

	public String getArtworkUrl() {
		return artworkUrl;
	}

	public void setArtworkUrl(String aArtworkUrl) {
		artworkUrl = aArtworkUrl;
	}
}
