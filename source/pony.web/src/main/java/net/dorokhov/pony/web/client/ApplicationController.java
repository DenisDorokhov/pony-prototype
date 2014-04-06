package net.dorokhov.pony.web.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public class ApplicationController {

	private PlaceController placeController;

	@Inject
	public void setPlaceController(PlaceController aPlaceController) {
		placeController = aPlaceController;
	}

	public void start() {
		// TODO: implement
	}

}
