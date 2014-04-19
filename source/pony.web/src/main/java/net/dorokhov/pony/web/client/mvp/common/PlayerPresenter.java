package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import net.dorokhov.pony.web.client.event.PlayListEvent;
import net.dorokhov.pony.web.client.event.SongEvent;
import net.dorokhov.pony.web.client.playlist.PlayList;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerPresenter extends PresenterWidget<PlayerPresenter.MyView> implements PlayerUiHandlers, PlayListEvent.Handler {

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

		public void play();
		public void pause();

		public State getState();

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final PlaceManager placeManager;

	private PlayList playList;

	@Inject
	public PlayerPresenter(EventBus aEventBus, MyView aView, PlaceManager aPlaceManager) {

		super(aEventBus, aView);

		placeManager = aPlaceManager;

		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {

		super.onBind();

		addRegisteredHandler(PlayListEvent.PLAYBACK_REQUESTED, this);
	}

	@Override
	public void onPlay() {

		log.fine("song " + getView().getSong() + " playback started");

		placeManager.setOnLeaveConfirmation("Playback will be stopped!");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_STARTED, getView().getSong()));
	}

	@Override
	public void onPause() {

		log.fine("song " + getView().getSong() + " playback paused");

		placeManager.setOnLeaveConfirmation(null);

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_PAUSED, getView().getSong()));
	}

	@Override
	public void onEnd() {

		log.fine("song " + getView().getSong() + " playback ended");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_ENDED, getView().getSong()));

		startNextSong();
	}

	@Override
	public void onPositionChange() {}

	@Override
	public void onVolumeChange() {}

	@Override
	public void onPlayListEvent(PlayListEvent aEvent) {

		playList = aEvent.getPlayList();

		startNextSong();
	}

	private void startNextSong() {

		if (playList != null) {

			playList.next(new AsyncCallback<SongDto>() {

				@Override
				public void onSuccess(SongDto aResult) {

					getView().setSong(aResult);

					if (aResult != null) {
						getView().play();
					}
				}

				@Override
				public void onFailure(Throwable aCaught) {

					log.log(Level.SEVERE, "could not fetch next song from playlist", aCaught);

					Window.alert(aCaught.getMessage());
				}
			});
		}
	}
}
