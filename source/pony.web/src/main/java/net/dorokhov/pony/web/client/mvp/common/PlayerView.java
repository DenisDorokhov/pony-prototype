package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.shared.SongDto;

public class PlayerView extends ViewWithUiHandlers<PlayerUiHandlers> implements PlayerPresenter.MyView {

	private class RootPanel extends SimpleLayoutPanel {

		@Override
		protected void onLoad() {

			super.onLoad();

			initPlayer(PLAYER_ID, createOptions().getJavaScriptObject());

			updateUnityOptions();
			sendUnityState(false);
		}
	}

	interface MyUiBinder extends UiBinder<Widget, PlayerView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final String PLAYER_ID = "jquery_jplayer_1";
	private static final String CONTAINER_ID = "jp_container_1";

	@UiField(provided = true)
	SimpleLayoutPanel rootPanel;

	@UiField(provided = true)
	String containerId = CONTAINER_ID;

	@UiField(provided = true)
	String playerId = PLAYER_ID;

	@UiField
	Label titleLabel;

	private State state;

	private double volume = 1.0;
	private double position = 0.0;

	private boolean previousSongAvailable;
	private boolean nextSongAvailable;

	private SongDto song;

	public PlayerView() {

		rootPanel = new RootPanel();

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public double getVolume() {
		return volume;
	}

	@Override
	public void setVolume(double aVolume) {

		volume = aVolume;

		updateVolume(PLAYER_ID, volume);
	}

	@Override
	public double getPosition() {
		return position;
	}

	@Override
	public void setPosition(double aPosition) {

		position = aPosition;

		updatePosition(PLAYER_ID, aPosition);
	}

	@Override
	public SongDto getSong() {
		return song;
	}

	@Override
	public void setSong(SongDto aSong) {

		song = aSong;

		if (song != null) {
			updateSong();
		}

		updateUnityOptions();
		sendUnityState(false);

		state = State.INACTIVE;
	}

	@Override
	public void play() {
		doPlay(PLAYER_ID);
	}

	@Override
	public void pause() {
		doPause(PLAYER_ID);
	}

	@Override
	public boolean isPreviousSongAvailable() {
		return previousSongAvailable;
	}

	@Override
	public void setPreviousSongAvailable(boolean aAvailable) {

		previousSongAvailable = aAvailable;

		updateUnityOptions();
	}

	@Override
	public boolean isNextSongAvailable() {
		return nextSongAvailable;
	}

	@Override
	public void setNextSongAvailable(boolean aAvailable) {

		nextSongAvailable = aAvailable;

		updateUnityOptions();
	}

	@Override
	public State getState() {
		return state;
	}

	@UiHandler("titleLabel")
	void onScanClick(ClickEvent aEvent) {
		getUiHandlers().onSongNavigationRequested();
	}

	private JSONObject createOptions() {

		JSONObject options = new JSONObject();

		options.put("swfPath", new JSONString("js/jplayer"));
		options.put("supplied", new JSONString("mp3"));
		options.put("wmode", new JSONString("window"));
		options.put("volume", new JSONNumber(volume));

		return options;
	}

	private native void initPlayer(String aPlayerId, JavaScriptObject aOptions) /*-{

		var self = this;

		aOptions.volumechange = function(event) {
			self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onVolumeChange(F)(event.jPlayer.options.volume);
		};
		aOptions.timeupdate = function(event) {
			self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPositionChange(F)(event.jPlayer.status.currentTime);
		};
		aOptions.play = function(event) {
			self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPlay()();
		};
		aOptions.pause = function(event) {
			self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPause()();
		};
		aOptions.ended = function(event) {
			self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onEnd()();
		};
		aOptions.error = function(event) {
			self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onError()();
		};

		$wnd.$("#" + aPlayerId).jPlayer(aOptions);

		$wnd.UnityMusicShim().setCallbackObject({
			pause: function() {
				self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPlayPause()();
			},
			next: function() {
				self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onNextRequested()();
			},
			previous: function() {
				self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPreviousRequested()();
			}
		});

		$wnd.$(".jp-play").click(function() {
			self.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPlayClick()();
		});
	}-*/;

	private void updateSong() {

		JSONObject options = new JSONObject();

		options.put("mp3", new JSONString(getSong().getFileUrl()));

		updateMedia(PLAYER_ID, CONTAINER_ID, options.getJavaScriptObject());

		setPosition(0.0);

		titleLabel.setText(song.getArtist() + " - " + song.getName());
	}

	private native void updateMedia(String aPlayerId, String aSkinId, JavaScriptObject aOptions) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("setMedia", aOptions);
	}-*/;

	private native void updateVolume(String aPlayerId, double aVolume) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("volume", aVolume);
	}-*/;

	private native void updatePosition(String aPlayerId, double aPosition) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("play", aPosition);
	}-*/;

	private native void doPlay(String aPlayerId) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("play");
	}-*/;

	private native void doPause(String aPlayerId) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("pause");
	}-*/;

	private void updateUnityOptions() {
		doUpdateUnityOptions(true, isPreviousSongAvailable(), isNextSongAvailable());
	}

	private native void doUpdateUnityOptions(boolean aCanPlay, boolean aPreviousSongAvailable, boolean aNextSongAvailable) /*-{
		$wnd.UnityMusicShim().setSupports({
			playpause: aCanPlay,
			next: aNextSongAvailable,
			previous: aPreviousSongAvailable
		});
	}-*/;

	private void sendUnityState(boolean aIsPlaying) {

		String name = null;
		String artist = null;
		String artworkUrl = null;

		if (getSong() != null) {
			name = getSong().getName();
			artist = getSong().getArtistName();
			artworkUrl = getSong().getAlbumArtworkUrl();
		}

		doSendUnityState(aIsPlaying, name, artist, artworkUrl);
	}

	private native void doSendUnityState(boolean aIsPlaying, String aName, String aArtist, String aArtwork) /*-{

		// TODO: show pony logo if no artwork defined: GWT.getHostPageBaseURL() + "img/logo.png";

		$wnd.UnityMusicShim().sendState({
			playing: aIsPlaying,
			title: aName,
			artist: aArtist,
			albumArt: aArtwork
		});
	}-*/;

	private void onVolumeChange(float aValue) {

		volume = aValue;

		getUiHandlers().onVolumeChange();
	}

	private void onPositionChange(float aValue) {

		position = aValue;

		getUiHandlers().onPositionChange();
	}

	private void onPlay() {

		state = State.PLAYING;

		sendUnityState(true);

		getUiHandlers().onPlay();
	}

	private void onPause() {

		state = State.PAUSED;

		sendUnityState(false);

		getUiHandlers().onPause();
	}

	private void onEnd() {

		state = State.INACTIVE;

		getUiHandlers().onEnd();
	}

	private void onError() {

		state = State.ERROR;

		getUiHandlers().onError();
	}

	private void onPlayPause() {
		if (getSong() != null) {
			if (state == State.PLAYING) {
				pause();
			} else {
				play();
			}
		} else {
			getUiHandlers().onPlaybackRequested();
		}
	}

	private void onPlayClick() {
		if (getSong() == null) {
			getUiHandlers().onPlaybackRequested();
		}
	}

	private void onPreviousRequested() {
		if (getPosition() >= 3) {
			setPosition(0.0);
		} else {
			getUiHandlers().onPreviousSongRequested();
		}
	}

	private void onNextRequested() {
		getUiHandlers().onNextSongRequested();
	}

}
