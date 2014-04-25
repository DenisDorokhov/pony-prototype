package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class PlayListImpl implements PlayList {

	private final Logger log = Logger.getLogger(getClass().getName());

	private ArrayList<SongDto> songs;

	private int currentIndex;

	public PlayListImpl(List<SongDto> aSongs, int aCurrentIndex) {

		songs = aSongs != null ? new ArrayList<SongDto>(aSongs) : new ArrayList<SongDto>();

		aCurrentIndex = Math.max(0, aCurrentIndex);
		aCurrentIndex = Math.min(songs.size() - 1, aCurrentIndex);

		currentIndex = aCurrentIndex;
	}

	public ArrayList<SongDto> getSongs() {
		return new ArrayList<SongDto>(songs);
	}

	@Override
	public boolean hasCurrent() {
		return songs.size() > 0;
	}

	@Override
	public boolean hasPrevious(Mode aMode) {

		if (songs.size() > 0) {
			if (aMode == Mode.NORMAL) {
				return currentIndex > 0 && songs.size() > 1;
			} else {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean hasNext(Mode aMode) {

		if (songs.size() > 0) {
			if (aMode == Mode.NORMAL) {
				return (currentIndex + 1) < songs.size();
			} else {
				return true;
			}
		}

		return false;
	}

	@Override
	public void current(AsyncCallback<SongDto> aCallback) {

		SongDto currentSong = null;

		if (songs.size() > 0) {
			currentSong = songs.get(currentIndex);
		}

		aCallback.onSuccess(currentSong);
	}

	@Override
	public void previous(Mode aMode, AsyncCallback<SongDto> aCallback) {

		SongDto currentSong = null;

		if (songs.size() > 0) {

			Integer switchToIndex = null;

			if (aMode == Mode.NORMAL) {

				if (currentIndex > 0 && songs.size() > 1) {
					switchToIndex = currentIndex - 1;
				}

			} else if (aMode == Mode.REPEAT_ALL) {

				if (currentIndex <= 0) {
					switchToIndex = songs.size() - 1;
				} else {
					switchToIndex = currentIndex - 1;
				}

			} else if (aMode == Mode.REPEAT_ONE) {
				switchToIndex = currentIndex;
			} else if (aMode == Mode.RANDOM) {
				switchToIndex = new Random().nextInt(songs.size());
			}

			if (switchToIndex != null) {

				currentIndex = switchToIndex;

				currentSong = songs.get(switchToIndex);
			}
		}

		if (currentSong != null) {
			log.fine("playlist switched to previous song " + currentSong);
		}

		aCallback.onSuccess(currentSong);
	}

	@Override
	public void next(Mode aMode, AsyncCallback<SongDto> aCallback) {

		SongDto currentSong = null;

		if (songs.size() > 0) {

			Integer switchToIndex = null;

			if (aMode == Mode.NORMAL) {

				if ((currentIndex + 1) < songs.size()) {
					switchToIndex = currentIndex + 1;
				}

			} else if (aMode == Mode.REPEAT_ALL) {

				if ((currentIndex + 1) >= songs.size()) {
					switchToIndex = 0;
				} else {
					switchToIndex = currentIndex + 1;
				}

			} else if (aMode == Mode.REPEAT_ONE) {
				switchToIndex = currentIndex;
			} else if (aMode == Mode.RANDOM) {
				switchToIndex = new Random().nextInt(songs.size());
			}

			if (switchToIndex != null) {

				currentIndex = switchToIndex;

				currentSong = songs.get(switchToIndex);
			}
		}

		if (currentSong != null) {
			log.fine("playlist switched to next song " + currentSong);
		}

		aCallback.onSuccess(currentSong);
	}
}
