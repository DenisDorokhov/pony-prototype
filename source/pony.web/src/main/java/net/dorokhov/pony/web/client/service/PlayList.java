package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.dorokhov.pony.web.shared.SongDto;

/**
 * Play list iterator.
 *
 * Allows navigation one by one through song list.
 */
public interface PlayList {

	public static enum Mode {
		NORMAL, REPEAT_ALL, REPEAT_ONE, RANDOM
	}

	/**
	 * Returns true if play list has current song.
	 *
	 * @return true if play list has current song, false otherwise
	 */
	public boolean hasCurrent();

	/**
	 * Returns true if play list has previous song.
	 *
	 * @return true if play list has previous song, false otherwise
	 */
	public boolean hasPrevious(Mode aMode);

	/**
	 * Returns true if play list has next song.
	 *
	 * @return true if play list has next song, false otherwise
	 */
	public boolean hasNext(Mode aMode);

	/**
	 * Fetches current song in play list. Operation can be asynchronous, but some implementations can execute callback immediately.
	 *
	 * @param aCallback song fetching callback, can return null if there is no current song available
	 */
	public void current(AsyncCallback<SongDto> aCallback);

	/**
	 * Fetches previous song in play list. Operation can be asynchronous, but some implementations can execute callback immediately.
	 *
	 * @param aCallback song fetching callback, can return null if there is no previous song available
	 */
	public void previous(Mode aMode, AsyncCallback<SongDto> aCallback);

	/**
	 * Fetches next song in play list. Operation can be asynchronous, but some implementations can execute callback immediately.
	 *
	 * @param aCallback song fetching callback, can return null if there is no next song available
	 */
	public void next(Mode aMode, AsyncCallback<SongDto> aCallback);

}
