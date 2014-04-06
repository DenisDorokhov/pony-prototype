package net.dorokhov.pony.web.client.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface MainView extends IsWidget {

	public AcceptsOneWidget getLogoContainer();

	public AcceptsOneWidget getPlayerContainer();

	public AcceptsOneWidget getSearchContainer();

	public AcceptsOneWidget getContentContainer();

}
