package net.dorokhov.pony.core.service.impl;

import net.dorokhov.pony.core.domain.SongData;
import net.dorokhov.pony.core.service.SongDataReader;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SongDataReaderImpl implements SongDataReader {

	@Override
	public SongData readSongData(File aFile) throws Exception {

		AudioFile audioFile = AudioFileIO.read(aFile);

		AudioHeader header = audioFile.getAudioHeader();
		Tag tag = audioFile.getTag();

		String mimeType = getMimeType(header);

		if (mimeType == null) {
			throw new Exception("Unsupported file format '" + header.getFormat() + "'.");
		}

		SongData metaData = new SongData();

		metaData.setPath(audioFile.getFile().getAbsolutePath());
		metaData.setFormat(header.getFormat());
		metaData.setMimeType(mimeType);
		metaData.setSize(audioFile.getFile().length());
		metaData.setDuration(header.getTrackLength());
		metaData.setBitRate(header.getBitRateAsNumber());

		if (tag != null) {

			metaData.setDiscNumber(parseIntegerTag(tag, FieldKey.DISC_NO));
			metaData.setDiscCount(parseIntegerTag(tag, FieldKey.DISC_TOTAL));

			metaData.setTrackNumber(parseIntegerTag(tag, FieldKey.TRACK));
			metaData.setTrackCount(parseIntegerTag(tag, FieldKey.TRACK_TOTAL));

			metaData.setName(parseStringTag(tag, FieldKey.TITLE));
			metaData.setAlbum(parseStringTag(tag, FieldKey.ALBUM));
			metaData.setYear(parseIntegerTag(tag, FieldKey.YEAR));

			String albumArtist = parseStringTag(tag, FieldKey.ALBUM_ARTIST);

			metaData.setArtist(albumArtist != null ? albumArtist : parseStringTag(tag, FieldKey.ARTIST));
		}

		return metaData;
	}

	private String parseStringTag(Tag aTag, FieldKey aKey) {
		return StringUtils.defaultIfBlank(aTag.getFirst(aKey), null);
	}

	private Integer parseIntegerTag(Tag aTag, FieldKey aKey) {

		Integer result = null;

		try {
			result = Integer.valueOf(aTag.getFirst(aKey));
		} catch (NumberFormatException e) {}

		return result;
	}

	private String getMimeType(AudioHeader aHeader) {

		String mimeType = null;

		if (aHeader.getFormat().equals("MPEG-1 Layer 3")) {
			mimeType = "audio/mpeg";
		}

		return mimeType;
	}
}
