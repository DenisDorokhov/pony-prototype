package net.dorokhov.pony.web.test;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.client.service.PlayListImpl;
import net.dorokhov.pony.web.shared.SongDto;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayListImplTest {

	@Test
	public void test() {

		List<SongDto> songList = new ArrayList<SongDto>();

		for (int i = 0; i < 10; i++) {
			songList.add(buildSong(i));
		}

		PlayListImpl playList = new PlayListImpl(songList, 9);

		Assert.assertTrue(playList.hasNext());
		doTestNextSong(playList, "song9");

		Assert.assertFalse(playList.hasNext());
		doTestNextSongNull(playList);

		Assert.assertTrue(playList.hasPrevious());
		doTestPreviousSong(playList, "song8");
		doTestPreviousSong(playList, "song7");
		doTestPreviousSong(playList, "song6");
		doTestPreviousSong(playList, "song5");
		doTestPreviousSong(playList, "song4");
		doTestPreviousSong(playList, "song3");
		doTestPreviousSong(playList, "song2");
		doTestPreviousSong(playList, "song1");
		doTestPreviousSong(playList, "song0");

		Assert.assertFalse(playList.hasPrevious());
		doTestPreviousSongNull(playList); // start of the list

		Assert.assertTrue(playList.hasNext());
		doTestNextSong(playList, "song1");
		doTestNextSong(playList, "song2");
	}

	private void doTestNextSong(PlayListImpl aPlayList, final String aName) {
		aPlayList.next(new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertEquals(aName, aSong.getName());
			}

			@Override
			public void onFailure(Throwable aCaught) {}
		});
	}

	private void doTestNextSongNull(PlayListImpl aPlayList) {
		aPlayList.next(new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertNull(aSong);
			}

			@Override
			public void onFailure(Throwable aCaught) {}
		});
	}

	private void doTestPreviousSong(PlayListImpl aPlayList, final String aName) {
		aPlayList.previous(new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertEquals(aName, aSong.getName());
			}

			@Override
			public void onFailure(Throwable aCaught) {}
		});
	}

	private void doTestPreviousSongNull(PlayListImpl aPlayList) {
		aPlayList.previous(new AsyncCallback<SongDto>() {

			@Override
			public void onSuccess(SongDto aSong) {
				Assert.assertNull(aSong);
			}

			@Override
			public void onFailure(Throwable aCaught) {}
		});
	}

	private SongDto buildSong(int aIndex) {

		SongDto song = new SongDto();

		song.setName("song" + aIndex);

		return song;
	}

}
