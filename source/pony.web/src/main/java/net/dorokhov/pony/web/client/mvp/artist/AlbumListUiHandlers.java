package net.dorokhov.pony.web.client.mvp.artist;

import com.gwtplatform.mvp.client.UiHandlers;
import net.dorokhov.pony.web.shared.SongDto;

public interface AlbumListUiHandlers extends UiHandlers {

	public void onSongSelected(SongDto aSong);

	public void onSongActivated(SongDto aSong);

}
