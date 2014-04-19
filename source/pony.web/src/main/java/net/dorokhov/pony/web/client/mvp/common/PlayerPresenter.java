package net.dorokhov.pony.web.client.mvp.common;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.dorokhov.pony.web.client.event.SongEvent;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.logging.Logger;

public class PlayerPresenter extends PresenterWidget<PlayerPresenter.MyView> implements PlayerUiHandlers, SongEvent.Handler {

	public interface MyView extends View, HasUiHandlers<PlayerUiHandlers> {

		public static enum State {
			INACTIVE, PLAYING, PAUSED
		}

		public double getVolume();

		public void setVolume(double aVolume);

		public double getPosition();

		public void setPosition(double aPosition);

		public SongDto getSong();

		public void setSong(SongDto aSong);

		public void start();
		public void pause();

		public State getState();

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	@Inject
	public PlayerPresenter(EventBus aEventBus, MyView aView) {

		super(aEventBus, aView);

		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {

		super.onBind();

		addRegisteredHandler(SongEvent.PLAYBACK_REQUESTED, this);
	}

	@Override
	public void onStart() {

		log.fine("song " + getView().getSong() + " playback started");

		getEventBus().fireEvent(new SongEvent(SongEvent.PLAYBACK_STARTED, getView().getSong()));
	}

	@Override
	public void onPause() {

		log.fine("song " + getView().getSong() + " playback paused");

		getEventBus().fireEvent(new SongEvent(SongEvent.PLAYBACK_PAUSED, getView().getSong()));
	}

	@Override
	public void onEnd() {

		log.fine("song " + getView().getSong() + " playback ended");

		getEventBus().fireEvent(new SongEvent(SongEvent.PLAYBACK_ENDED, getView().getSong()));
	}

	@Override
	public void onPositionChange() {}

	@Override
	public void onVolumeChange() {}

	@Override
	public void onSongEvent(SongEvent aEvent) {

		getView().setSong(aEvent.getSong());

		getView().start();
	}
}
