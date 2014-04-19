package net.dorokhov.pony.web.client.playlist;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlayListImpl implements PlayList {

	private final Logger log = Logger.getLogger(getClass().getName());

	private ArrayList<SongDto> songs;

	private int currentIndex;

	public PlayListImpl(List<SongDto> aSongs, int aStartIndex) {

		songs = aSongs != null ? new ArrayList<SongDto>(aSongs) : new ArrayList<SongDto>();

		currentIndex = aStartIndex;
	}

	public ArrayList<SongDto> getSongs() {
		return songs;
	}

	@Override
	public void next(AsyncCallback<SongDto> aCallback) {

		SongDto currentSong = null;

		if (currentIndex < songs.size()) {

			currentSong = songs.get(currentIndex);

			currentIndex++;
		}

		if (currentSong != null) {
			log.info("playlist switched to song " + currentSong);
		}

		aCallback.onSuccess(currentSong);
	}

	@Override
	public void reset() {
		currentIndex = 0;
	}
}
