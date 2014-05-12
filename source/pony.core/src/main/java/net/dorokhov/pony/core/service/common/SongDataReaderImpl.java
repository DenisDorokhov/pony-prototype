package net.dorokhov.pony.core.service.common;

import net.dorokhov.pony.core.domain.SongData;
import net.dorokhov.pony.core.service.ChecksumService;
import net.dorokhov.pony.core.service.SongDataReader;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SongDataReaderImpl implements SongDataReader {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private ChecksumService checksumService;

	@Autowired
	public void setChecksumService(ChecksumService aChecksumService) {
		checksumService = aChecksumService;
	}

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

			metaData.setName(StringUtils.trim(parseStringTag(tag, FieldKey.TITLE)));
			metaData.setAlbum(StringUtils.trim(parseStringTag(tag, FieldKey.ALBUM)));
			metaData.setYear(parseIntegerTag(tag, FieldKey.YEAR));

			metaData.setArtist(StringUtils.trim(parseStringTag(tag, FieldKey.ARTIST)));
			metaData.setAlbumArtist(StringUtils.trim(parseStringTag(tag, FieldKey.ALBUM_ARTIST)));

			metaData.setGenre(StringUtils.trim(parseStringTag(tag, FieldKey.GENRE)));

			Artwork artwork = tag.getFirstArtwork();

			if (artwork != null) {
				metaData.setArtwork(new SongData.Artwork(artwork.getBinaryData(), checksumService.calculateChecksum(artwork.getBinaryData()), artwork.getMimeType()));
			}
		}

		log.debug("read file data: {}", metaData);

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
