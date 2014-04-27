package net.dorokhov.pony.web.client.mvp.common;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ToolbarUiHandlers extends UiHandlers {

	public void onRefreshRequested();

	public void onSettingsRequested();

}
