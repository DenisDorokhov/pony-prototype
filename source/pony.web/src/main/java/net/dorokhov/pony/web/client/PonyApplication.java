package net.dorokhov.pony.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import net.dorokhov.pony.web.client.ui.MainView;

public class PonyApplication implements EntryPoint {

	@Override
	public void onModuleLoad() {

		MainView mainView = new MainView();

		RootPanel.get("mainView").add(mainView);
	}
}
