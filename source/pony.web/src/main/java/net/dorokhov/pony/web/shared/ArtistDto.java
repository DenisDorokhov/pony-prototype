package net.dorokhov.pony.web.shared;

import java.io.Serializable;

public class ArtistDto extends AbstractEntityDto implements Serializable {

	private String name;

	private Long artwork;

	private String artworkUrl;

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	public Long getArtwork() {
		return artwork;
	}

	public void setArtwork(Long aArtwork) {
		artwork = aArtwork;
	}

	public String getArtworkUrl() {
		return artworkUrl;
	}

	public void setArtworkUrl(String aArtworkUrl) {
		artworkUrl = aArtworkUrl;
	}

	@Override
	public String toString() {
		return "ArtistDto{" +
				"id=" + getId() +
				", name='" + name + '\'' +
				'}';
	}
}
