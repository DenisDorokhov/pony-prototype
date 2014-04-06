package net.dorokhov.pony.web.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import net.dorokhov.pony.web.client.mapper.*;
import net.dorokhov.pony.web.client.place.ArtistsPlace;
import net.dorokhov.pony.web.client.view.MainView;

public class Bootstrap {

	private PlaceController placeController;

	private EventBus eventBus;

	private ApplicationHistoryMapper historyMapper;

	private LogoActivityMapper logoActivityMapper;
	private PlayerActivityMapper playerActivityMapper;
	private SearchActivityMapper searchActivityMapper;
	private ArtistsActivityMapper artistsActivityMapper;

	private MainView mainView;

	@Inject
	public void setPlaceController(PlaceController aPlaceController) {
		placeController = aPlaceController;
	}

	@Inject
	public void setEventBus(EventBus aEventBus) {
		eventBus = aEventBus;
	}

	@Inject
	public void setHistoryMapper(ApplicationHistoryMapper aHistoryMapper) {
		historyMapper = aHistoryMapper;
	}

	@Inject
	public void setLogoActivityMapper(LogoActivityMapper aLogoActivityMapper) {
		logoActivityMapper = aLogoActivityMapper;
	}

	@Inject
	public void setPlayerActivityMapper(PlayerActivityMapper aPlayerActivityMapper) {
		playerActivityMapper = aPlayerActivityMapper;
	}

	@Inject
	public void setSearchActivityMapper(SearchActivityMapper aSearchActivityMapper) {
		searchActivityMapper = aSearchActivityMapper;
	}

	@Inject
	public void setArtistsActivityMapper(ArtistsActivityMapper aArtistsActivityMapper) {
		artistsActivityMapper = aArtistsActivityMapper;
	}

	@Inject
	public void setMainView(MainView aMainView) {
		mainView = aMainView;
	}

	public void run() {

		initActivityManagers();

		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);

		historyHandler.register(placeController, eventBus, new ArtistsPlace());

		RootLayoutPanel.get().add(mainView);

		historyHandler.handleCurrentHistory();
	}

	private void initActivityManagers() {

		ActivityManager logoManager = new ActivityManager(logoActivityMapper, eventBus);
		logoManager.setDisplay(mainView.getLogoContainer());

		ActivityManager playerManager = new ActivityManager(playerActivityMapper, eventBus);
		playerManager.setDisplay(mainView.getPlayerContainer());

		ActivityManager searchManager = new ActivityManager(searchActivityMapper, eventBus);
		searchManager.setDisplay(mainView.getSearchContainer());

		ActivityManager artistsManager = new ActivityManager(artistsActivityMapper, eventBus);
		artistsManager.setDisplay(mainView.getContentContainer());
	}

}
