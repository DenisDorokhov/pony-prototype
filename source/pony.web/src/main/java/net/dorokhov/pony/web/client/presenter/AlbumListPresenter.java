package net.dorokhov.pony.web.client.presenter;

import net.dorokhov.pony.web.shared.SongDto;

public interface AlbumListPresenter {

	public void onSongSelected(SongDto aSong);
	public void onSongActivated(SongDto aSong);

}
