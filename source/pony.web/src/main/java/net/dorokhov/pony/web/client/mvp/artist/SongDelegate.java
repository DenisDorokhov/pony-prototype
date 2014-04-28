package net.dorokhov.pony.web.client.mvp.artist;

import net.dorokhov.pony.web.shared.SongDto;

public interface SongDelegate {

	public void onSongSelected(SongDto aSong);

	public void onSongPlaybackRequested(SongDto aSong);

}
