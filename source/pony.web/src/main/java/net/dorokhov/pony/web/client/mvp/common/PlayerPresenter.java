package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.dorokhov.pony.web.client.Messages;
import net.dorokhov.pony.web.client.event.PlayListEvent;
import net.dorokhov.pony.web.client.event.PlaybackEvent;
import net.dorokhov.pony.web.client.event.SongEvent;
import net.dorokhov.pony.web.client.service.PlayListNavigator;
import net.dorokhov.pony.web.client.service.PlayListNavigatorImpl;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.logging.Logger;

public class PlayerPresenter extends PresenterWidget<PlayerPresenter.MyView> implements PlayerUiHandlers, PlayListEvent.Handler, Window.ClosingHandler {

	public interface MyView extends View, HasUiHandlers<PlayerUiHandlers> {

		public static enum State {
			INACTIVE, PLAYING, PAUSED, ERROR
		}

		public double getVolume();

		public void setVolume(double aVolume);

		public double getPosition();

		public void setPosition(double aPosition);

		public SongDto getSong();

		public void setSong(SongDto aSong);

		public void play();
		public void pause();

		public boolean isPreviousSongAvailable();
		public void setPreviousSongAvailable(boolean aAvailable);

		public boolean isNextSongAvailable();
		public void setNextSongAvailable(boolean aAvailable);

		public State getState();

	}

	private final Logger log = Logger.getLogger(getClass().getName());

	private final PlayListNavigator playListNavigator = new PlayListNavigatorImpl(PlayListNavigatorImpl.Mode.NORMAL);

	private HandlerRegistration closingWindowRegistration;

	@Inject
	public PlayerPresenter(EventBus aEventBus, MyView aView) {

		super(aEventBus, aView);

		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {

		super.onBind();

		addRegisteredHandler(PlayListEvent.PLAYLIST_CHANGE, this);
	}

	@Override
	protected void onReveal() {

		super.onReveal();

		closingWindowRegistration = Window.addWindowClosingHandler(this);
	}

	@Override
	protected void onHide() {

		super.onHide();

		closingWindowRegistration.removeHandler();
	}

	@Override
	public void onPlay() {

		log.fine("song " + getView().getSong() + " playback started");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_STARTED, getView().getSong()));
	}

	@Override
	public void onPause() {

		log.fine("song " + getView().getSong() + " playback paused");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_PAUSED, getView().getSong()));
	}

	@Override
	public void onEnd() {

		log.fine("song " + getView().getSong() + " playback ended");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_ENDED, getView().getSong()));

		playNextSong();
	}

	@Override
	public void onError() {

		log.fine("song " + getView().getSong() + " playback failed");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_FAILED, getView().getSong()));
	}

	@Override
	public void onPositionChange() {}

	@Override
	public void onVolumeChange() {}

	@Override
	public void onPlaybackRequested() {
		getEventBus().fireEvent(new PlaybackEvent(PlaybackEvent.PLAYBACK_REQUESTED));
	}

	@Override
	public void onPreviousSongRequested() {
		playPreviousSong();
	}

	@Override
	public void onNextSongRequested() {
		playNextSong();
	}

	@Override
	public void onSongNavigationRequested() {

		log.fine("song " + getView().getSong() + " selection requested");

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_SELECTION_REQUESTED, getView().getSong()));
	}

	@Override
	public void onPlayListEvent(PlayListEvent aEvent) {

		playListNavigator.setPlayList(aEvent.getPlayList());
		playListNavigator.setCurrentIndex(aEvent.getStartIndex());

		getView().setPreviousSongAvailable(false);
		getView().setNextSongAvailable(false);

		playCurrentSong();
	}

	@Override
	public void onWindowClosing(Window.ClosingEvent aEvent) {
		if (getView().getState() == MyView.State.PLAYING) {
			aEvent.setMessage(Messages.IMPL.alertPlaybackWillBeStopped());
		}
	}

	private void playCurrentSong() {

		SongDto song = playListNavigator.getCurrent();

		if (song != null) {
			doPlaySong(song);
		}
	}

	private void playPreviousSong() {

		SongDto song = playListNavigator.switchToPrevious();

		if (song != null) {
			doPlaySong(song);
		}
	}

	private void playNextSong() {

		SongDto song = playListNavigator.switchToNext();

		if (song != null) {
			doPlaySong(song);
		}
	}

	private void doPlaySong(SongDto aSong) {

		getView().setSong(aSong);

		getEventBus().fireEvent(new SongEvent(SongEvent.SONG_CHANGED, aSong));

		getView().setPreviousSongAvailable(playListNavigator.hasPrevious());
		getView().setNextSongAvailable(playListNavigator.hasNext());

		getView().play();
	}
}
