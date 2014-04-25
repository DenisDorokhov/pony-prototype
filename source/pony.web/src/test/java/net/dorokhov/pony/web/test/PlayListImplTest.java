package net.dorokhov.pony.web.test;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.client.service.PlayList;
import net.dorokhov.pony.web.client.service.PlayListImpl;
import net.dorokhov.pony.web.shared.SongDto;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayListImplTest {

	@Test
	public void testInconsistency() {

		doTestEmpty(PlayList.Mode.NORMAL);
		doTestEmpty(PlayList.Mode.REPEAT_ALL);
		doTestEmpty(PlayList.Mode.REPEAT_ONE);
		doTestEmpty(PlayList.Mode.RANDOM);

		List<SongDto> songList = new ArrayList<SongDto>();

		for (int i = 0; i < 10; i++) {
			songList.add(buildSong(i));
		}

		PlayListImpl playList = new PlayListImpl(songList, 10);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 9L);

		playList = new PlayListImpl(songList, -1);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 0L);
	}

	@Test
	public void testNormal() {

		List<SongDto> songList = new ArrayList<SongDto>();

		for (int i = 0; i < 10; i++) {
			songList.add(buildSong(i));
		}

		PlayList.Mode mode = PlayList.Mode.NORMAL;

		PlayListImpl playList = new PlayListImpl(songList, 9);

		Assert.assertEquals(songList, playList.getSongs());

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 9L);

		Assert.assertFalse(playList.hasNext(mode));
		doTestNextSongNull(playList, mode);

		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 8L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 7L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 6L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 5L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 4L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 3L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 2L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 1L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 0L);

		Assert.assertFalse(playList.hasPrevious(mode));
		doTestPreviousSongNull(playList, mode);

		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 1L);
		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 2L);

		playList = new PlayListImpl(songList, 0);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 0L);

		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 1L);
		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 2L);
	}

	@Test
	public void testRepeatAll() {

		List<SongDto> songList = new ArrayList<SongDto>();

		for (int i = 0; i < 10; i++) {
			songList.add(buildSong(i));
		}

		PlayList.Mode mode = PlayList.Mode.REPEAT_ALL;

		PlayListImpl playList = new PlayListImpl(songList, 9);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 9L);

		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 0L);
		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 1L);

		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 0L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 9L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 8L);

		playList = new PlayListImpl(songList, 0);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 0L);

		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 1L);
		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 2L);
	}

	@Test
	public void testRepeatOne() {

		List<SongDto> songList = new ArrayList<SongDto>();

		for (int i = 0; i < 10; i++) {
			songList.add(buildSong(i));
		}

		PlayList.Mode mode = PlayList.Mode.REPEAT_ONE;

		PlayListImpl playList = new PlayListImpl(songList, 0);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 0L);

		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 0L);
		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 0L);

		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 0L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 0L);

		playList = new PlayListImpl(songList, 9);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 9L);

		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 9L);
		Assert.assertTrue(playList.hasNext(mode));
		doTestNextSong(playList, mode, 9L);

		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 9L);
		Assert.assertTrue(playList.hasPrevious(mode));
		doTestPreviousSong(playList, mode, 9L);
	}

	@Test
	public void testRandom() {

		List<SongDto> songList = new ArrayList<SongDto>();

		for (int i = 0; i < 10; i++) {
			songList.add(buildSong(i));
		}

		PlayListImpl playList = new PlayListImpl(songList, 0);

		Assert.assertTrue(playList.hasCurrent());
		doTestCurrentSong(playList, 0L);

		doTestRandomNextSong(playList);
		doTestRandomNextSong(playList);
		doTestRandomNextSong(playList);

		doTestRandomPreviousSong(playList);
		doTestRandomPreviousSong(playList);
		doTestRandomPreviousSong(playList);
	}

	private void doTestEmpty(PlayList.Mode aMode) {

		PlayListImpl playList = new PlayListImpl(new ArrayList<SongDto>(), 9);

		Assert.assertFalse(playList.hasCurrent());
		doTestCurrentSongNull(playList);

		Assert.assertFalse(playList.hasNext(aMode));
		doTestNextSongNull(playList, aMode);

		Assert.assertFalse(playList.hasPrevious(aMode));
		doTestPreviousSongNull(playList, aMode);
	}

	private void doTestCurrentSong(PlayListImpl aPlayList, final Long aId) {
		aPlayList.getCurrent(new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertEquals(aId, aSong.getId());
			}

			@Override
			public void onFailure(Throwable aCaught) {}

		});
	}

	private void doTestCurrentSongNull(PlayListImpl aPlayList) {
		aPlayList.getCurrent(new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertNull(aSong);
			}

			@Override
			public void onFailure(Throwable aCaught) {
			}

		});
	}

	private void doTestPreviousSong(PlayListImpl aPlayList, PlayList.Mode aMode, final Long aId) {
		aPlayList.getPrevious(aMode, new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertEquals(aId, aSong.getId());
			}

			@Override
			public void onFailure(Throwable aCaught) {}

		});
	}

	private void doTestPreviousSongNull(PlayListImpl aPlayList, PlayList.Mode aMode) {
		aPlayList.getPrevious(aMode, new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertNull(aSong);
			}

			@Override
			public void onFailure(Throwable aCaught) {}

		});
	}

	private void doTestNextSong(PlayListImpl aPlayList, PlayList.Mode aMode, final Long aId) {
		aPlayList.getNext(aMode, new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertEquals(aId, aSong.getId());
			}

			@Override
			public void onFailure(Throwable aCaught) {}

		});
	}

	private void doTestNextSongNull(PlayListImpl aPlayList, PlayList.Mode aMode) {
		aPlayList.getNext(aMode, new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertNull(aSong);
			}

			@Override
			public void onFailure(Throwable aCaught) {}

		});
	}

	private void doTestRandomPreviousSong(PlayListImpl aPlayList) {
		aPlayList.getPrevious(PlayList.Mode.RANDOM, new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertNotNull(aSong);
			}

			@Override
			public void onFailure(Throwable aCaught) {}

		});
	}

	private void doTestRandomNextSong(PlayListImpl aPlayList) {
		aPlayList.getNext(PlayList.Mode.RANDOM, new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertNotNull(aSong);
			}

			@Override
			public void onFailure(Throwable aCaught) {}

		});
	}

	private SongDto buildSong(int aIndex) {

		SongDto song = new SongDto();

		song.setId((long) aIndex);

		return song;
	}

}
