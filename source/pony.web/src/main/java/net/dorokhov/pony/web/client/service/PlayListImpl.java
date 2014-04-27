package net.dorokhov.pony.web.client.service;

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

		currentIndex = aStartIndex - 1;
	}

	public ArrayList<SongDto> getSongs() {
		return new ArrayList<SongDto>(songs);
	}

	@Override
	public boolean hasPrevious() {
		return currentIndex > 0 && songs.size() > 1;
	}

	@Override
	public boolean hasNext() {
		return (currentIndex + 1) < songs.size();
	}

	@Override
	public void previous(AsyncCallback<SongDto> aCallback) {

		SongDto currentSong = null;

		if (hasPrevious()) {

			currentIndex--;

			currentSong = songs.get(currentIndex);
		}

		if (currentSong != null) {
			log.fine("playlist switched to previous song " + currentSong);
		}

		aCallback.onSuccess(currentSong);
	}

	@Override
	public void next(AsyncCallback<SongDto> aCallback) {

		SongDto currentSong = null;

		if (hasNext()) {

			currentIndex++;

			currentSong = songs.get(currentIndex);
		}

		if (currentSong != null) {
			log.fine("playlist switched to next song " + currentSong);
		}

		aCallback.onSuccess(currentSong);
	}

	@Override
	public void reset() {
		currentIndex = -1;
	}
}
