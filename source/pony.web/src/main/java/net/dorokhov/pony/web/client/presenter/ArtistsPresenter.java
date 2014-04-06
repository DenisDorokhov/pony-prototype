package net.dorokhov.pony.web.client.presenter;

import net.dorokhov.pony.web.shared.ArtistDto;
import net.dorokhov.pony.web.shared.SongDto;

public interface ArtistsPresenter {

	public void onArtistSelected(ArtistDto aArtist);

	public void onSongSelected(SongDto aSong);

	public void onSongActivated(SongDto aSong);

}
