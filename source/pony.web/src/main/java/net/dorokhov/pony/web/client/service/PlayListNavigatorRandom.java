package net.dorokhov.pony.web.client.service;

import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayListNavigatorRandom implements PlayListNavigator, PlayList.Delegate {

	private static final int QUEUE_SIZE = 20;

	private PlayList playList;

	private List<SongDto> pastQueue = new ArrayList<SongDto>();
	private List<SongDto> futureQueue = new ArrayList<SongDto>();

	private SongDto currentSong;

	@Override
	public PlayList getPlayList() {
		return playList;
	}

	@Override
	public void setPlayList(PlayList aPlayList) {

		if (playList != aPlayList) {

			if (playList != null) {
				playList.removeDelegate(this);
			}

			playList = aPlayList;

			if (playList != null) {
				playList.addDelegate(this);
			}

			pastQueue.clear();
			futureQueue.clear();

			checkAndUpdateFutureQueue();

			if (futureQueue.size() > 0) {
				currentSong = futureQueue.remove(0);
			}
		}
	}

	@Override
	public List<SongDto> getQueue() {
		return new ArrayList<SongDto>(futureQueue);
	}

	@Override
	public SongDto getCurrent() {
		return currentSong;
	}

	@Override
	public SongDto setCurrentIndex(int aIndex) {

		if (playList == null || aIndex < 0 || aIndex >= playList.size()) {
			throw new IndexOutOfBoundsException();
		}

		currentSong = playList.get(aIndex);

		pastQueue.clear();
		futureQueue.clear();

		checkAndUpdateFutureQueue();

		return currentSong;
	}

	@Override
	public boolean hasPrevious() {
		return pastQueue.size() > 0;
	}

	@Override
	public boolean hasNext() {
		return futureQueue.size() > 0;
	}

	@Override
	public SongDto switchToPrevious() {

		if (pastQueue.size() > 0) {

			futureQueue.add(0, currentSong);

			currentSong = pastQueue.remove(pastQueue.size() - 1);

			return getCurrent();
		}

		return null;
	}

	@Override
	public SongDto switchToNext() {

		if (futureQueue.size() > 0) {

			pastQueue.add(currentSong);

			currentSong = futureQueue.remove(0);

			checkAndUpdateFutureQueue();

			return getCurrent();
		}

		return null;
	}

	@Override
	public void onPlayListSongAdded(PlayList aPlayList, SongDto aSong, int aIndex) {
		if (playList == aPlayList) {
			checkAndUpdateFutureQueue();
		}
	}

	@Override
	public void onPlayListSongRemoved(PlayList aPlayList, SongDto aSong, int aIndex) {
		if (playList == aPlayList) {

			pastQueue.remove(aSong);
			futureQueue.remove(aSong);

			checkAndUpdateFutureQueue();
		}
	}

	@Override
	public void onPlayListSongMoved(PlayList aPlayList, int aOldIndex, int aNewIndex) {}

	private void checkAndUpdateFutureQueue() {
		if (playList != null && playList.size() > 0) {

			Random random = new Random();

			while (futureQueue.size() < QUEUE_SIZE) {
				futureQueue.add(playList.get(random.nextInt(playList.size())));
			}
		}
	}
}
