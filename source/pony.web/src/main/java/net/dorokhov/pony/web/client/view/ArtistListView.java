package net.dorokhov.pony.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import net.dorokhov.pony.web.client.common.ContentView;
import net.dorokhov.pony.web.client.common.PresenterView;
import net.dorokhov.pony.web.client.presenter.ArtistListPresenter;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.List;

public interface ArtistListView extends PresenterView<ArtistListPresenter>, ContentView, IsWidget {

	public List<ArtistDto> getArtists();

	public void setArtists(List<ArtistDto> aArtists);

	public ArtistDto getSelectedArtist();

	public void setSelectedArtist(ArtistDto aArtist);

}
