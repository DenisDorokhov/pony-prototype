package net.dorokhov.pony.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

public interface MainView extends IsWidget {

	public SimpleLayoutPanel getLogoContainer();

	public SimpleLayoutPanel getPlayerContainer();

	public SimpleLayoutPanel getSearchContainer();

	public SimpleLayoutPanel getContentContainer();

}
