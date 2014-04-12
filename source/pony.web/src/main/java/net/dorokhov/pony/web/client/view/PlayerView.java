package net.dorokhov.pony.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import net.dorokhov.pony.web.shared.SongDto;

public interface PlayerView extends IsWidget {

	public static enum State {
		INACTIVE, PLAYING, PAUSED
	}

	public static interface Delegate {

		public void onStart(PlayerView aPlayerView);
		public void onPause(PlayerView aPlayerView);
		public void onResume(PlayerView aPlayerView);
		public void onEnd(PlayerView aPlayerView);

		public void onPositionChange(PlayerView aPlayerView);

		public void onVolumeChange(PlayerView aPlayerView);
	}

	public double getVolume();

	public void setVolume(double aVolume);

	public double getPosition();

	public void setPosition(double aPosition);

	public SongDto getSong();

	public void setSong(SongDto aSong);

	public void start();
	public void pause();
	public void resume();

	public State getState();

	public Delegate getDelegate();

	public void setDelegate(Delegate aDelegate);

}
