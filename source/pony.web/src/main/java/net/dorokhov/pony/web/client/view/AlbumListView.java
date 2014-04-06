package net.dorokhov.pony.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import net.dorokhov.pony.web.client.common.ContentView;
import net.dorokhov.pony.web.client.common.PresenterView;
import net.dorokhov.pony.web.client.presenter.AlbumListPresenter;
import net.dorokhov.pony.web.shared.AlbumSongsDto;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.List;

public interface AlbumListView extends PresenterView<AlbumListPresenter>, ContentView, IsWidget {

	public List<AlbumSongsDto> getAlbums();

	public void setAlbums(List<AlbumSongsDto> aAlbums);

	public SongDto getSelectedSong();

	public void setSelectedSong(SongDto aSong);

}
