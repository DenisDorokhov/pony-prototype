package net.dorokhov.pony.web.test;

import net.dorokhov.pony.web.client.service.PlayListImpl;
import net.dorokhov.pony.web.client.service.PlayListNavigatorImpl;
import net.dorokhov.pony.web.shared.SongDto;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PlayListNavigatorImplTest {

	@Test
	public void testNormal() {

		PlayListImpl playList = new PlayListImpl();

		for (int i = 0; i < 10; i++) {
			playList.add(buildSong(i));
		}

		PlayListNavigatorImpl navigator = new PlayListNavigatorImpl(playList);

		Assert.assertTrue(navigator.getPlayList() == playList);

		navigator.setCurrentIndex(9);

		Assert.assertEquals(Long.valueOf(9), navigator.getCurrent().getId());

		Assert.assertFalse(navigator.hasNext());
		Assert.assertNull(navigator.switchToNext());

		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(8), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(7), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(6), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(5), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(4), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(3), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(2), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(1), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(0), navigator.switchToPrevious().getId());

		Assert.assertFalse(navigator.hasPrevious());
		Assert.assertNull(navigator.switchToPrevious());

		Assert.assertTrue(navigator.hasNext());
		Assert.assertEquals(Long.valueOf(1), navigator.switchToNext().getId());
		Assert.assertTrue(navigator.hasNext());
		Assert.assertEquals(Long.valueOf(2), navigator.switchToNext().getId());

		List<SongDto> queue = navigator.getQueue();

		Assert.assertEquals(7, queue.size());

		Assert.assertEquals(Long.valueOf(3), queue.get(0).getId());
		Assert.assertEquals(Long.valueOf(4), queue.get(1).getId());
		Assert.assertEquals(Long.valueOf(5), queue.get(2).getId());
		Assert.assertEquals(Long.valueOf(6), queue.get(3).getId());
		Assert.assertEquals(Long.valueOf(7), queue.get(4).getId());
		Assert.assertEquals(Long.valueOf(8), queue.get(5).getId());
		Assert.assertEquals(Long.valueOf(9), queue.get(6).getId());
	}

	@Test
	public void testRepeatAll() {

		PlayListImpl playList = new PlayListImpl();

		for (int i = 0; i < 10; i++) {
			playList.add(buildSong(i));
		}

		PlayListNavigatorImpl navigator = new PlayListNavigatorImpl();

		navigator.setMode(PlayListNavigatorImpl.Mode.REPEAT_ALL);
		navigator.setPlayList(playList);
		navigator.setCurrentIndex(9);

		Assert.assertEquals(Long.valueOf(9), navigator.getCurrent().getId());

		Assert.assertTrue(navigator.hasNext());
		Assert.assertEquals(Long.valueOf(0), navigator.switchToNext().getId());
		Assert.assertTrue(navigator.hasNext());
		Assert.assertEquals(Long.valueOf(1), navigator.switchToNext().getId());

		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(0), navigator.switchToPrevious().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(9), navigator.switchToPrevious().getId());

		List<SongDto> queue = navigator.getQueue();

		Assert.assertEquals(20, queue.size());

		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(Long.valueOf(i), queue.get(i).getId());
		}
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals(Long.valueOf(i), queue.get(10 + i).getId());
		}
	}

	@Test
	public void testRepeatOne() {

		PlayListImpl playList = new PlayListImpl();

		for (int i = 0; i < 10; i++) {
			playList.add(buildSong(i));
		}

		PlayListNavigatorImpl navigator = new PlayListNavigatorImpl(PlayListNavigatorImpl.Mode.REPEAT_ONE, playList);

		Assert.assertEquals(Long.valueOf(0), navigator.getCurrent().getId());

		Assert.assertTrue(navigator.hasNext());
		Assert.assertEquals(Long.valueOf(0), navigator.switchToNext().getId());
		Assert.assertTrue(navigator.hasPrevious());
		Assert.assertEquals(Long.valueOf(0), navigator.switchToPrevious().getId());

		List<SongDto> queue = navigator.getQueue();

		Assert.assertEquals(20, queue.size());

		for (int i = 0; i < 20; i++) {
			Assert.assertEquals(Long.valueOf(0), queue.get(i).getId());
		}
	}

	private SongDto buildSong(int aIndex) {

		SongDto song = new SongDto();

		song.setId((long) aIndex);

		return song;
	}

}
