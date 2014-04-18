package net.dorokhov.pony.web.client.mvp.common;

import com.gwtplatform.mvp.client.UiHandlers;

public interface PlayerUiHandlers extends UiHandlers {

	public void onStart();
	public void onPause();
	public void onEnd();

	public void onPositionChange();

	public void onVolumeChange();

}
