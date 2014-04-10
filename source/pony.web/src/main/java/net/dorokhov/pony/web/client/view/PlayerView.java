package net.dorokhov.pony.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface PlayerView extends IsWidget {

	public static enum State {
		STOPPED, PLAYING, PAUSED
	}

	public double getVolume();

	public void setVolume(double aVolume);

	public double getPosition();

	public void setPosition(double aPosition);

	public State getState();

	public void setState(State aState);

}
