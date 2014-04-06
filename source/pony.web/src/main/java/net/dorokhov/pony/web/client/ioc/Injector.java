package net.dorokhov.pony.web.client.ioc;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import net.dorokhov.pony.web.client.ApplicationController;
import net.dorokhov.pony.web.client.presenter.impl.AlbumListActivity;
import net.dorokhov.pony.web.client.presenter.impl.ArtistListActivity;

@GinModules(Module.class)
public interface Injector extends Ginjector {

	public ApplicationController getApplicationController();

	public ArtistListActivity getArtistListPresenter();

	public AlbumListActivity getAlbumListPresenter();

}
