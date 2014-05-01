package net.dorokhov.pony.web.client.mvp.artists;

import com.gwtplatform.mvp.client.UiHandlers;
import net.dorokhov.pony.web.shared.SongDto;

public interface AlbumListUiHandlers extends UiHandlers {

	public void onSongSelection(SongDto aSong);

	public void onSongActivation(SongDto aSong);

}
