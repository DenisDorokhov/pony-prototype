package net.dorokhov.pony.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import net.dorokhov.pony.web.client.common.PresenterView;
import net.dorokhov.pony.web.client.presenter.PlayerPresenter;

public interface PlayerView extends PresenterView<PlayerPresenter>, IsWidget {

	public double getVolume();

	public void setVolume(double aVolume);

	public double getPosition();

	public void setPosition(double aPosition);

	public State getState();

	public void setState(State aState);

	public static enum State {
		STOPPED, PLAYING, PAUSED
	}

}
