package net.dorokhov.pony.core.entity;

import net.dorokhov.pony.core.entity.common.BaseEntityIdentified;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "song")
public class Song extends BaseEntityIdentified {

	private String name;

	private Long duration;

	private Integer trackNumber;

	private Integer discNumber;

	private AudioFile audioFile;

	private Album album;

	private Artist artist;

	@Column(name = "name")
	@NotBlank
	@Size(max = 255)
	public String getName() {
		return name;
	}

	public void setName(String aName) {
		name = aName;
	}

	@Column(name = "duration")
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long aDuration) {
		duration = aDuration;
	}

	@Column(name = "track_number")
	public Integer getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(Integer aTrackNumber) {
		trackNumber = aTrackNumber;
	}

	@Column(name = "disc_number")
	public Integer getDiscNumber() {
		return discNumber;
	}

	public void setDiscNumber(Integer aDiscNumber) {
		discNumber = aDiscNumber;
	}

	@OneToOne
	@JoinColumn(name = "id")
	public AudioFile getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(AudioFile aAudioFile) {
		audioFile = aAudioFile;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "album_id")
	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album aAlbum) {
		album = aAlbum;
	}

	@ManyToOne(optional = true)
	@JoinColumn(name = "artist_id")
	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist aArtist) {
		artist = aArtist;
	}
}
