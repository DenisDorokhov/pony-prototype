package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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

	private State state;

	private double volume = 1.0;

	private double position = 0.0;

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

			JSONObject options = new JSONObject();

			options.put("mp3", new JSONString(aSong.getFileUrl()));

			updateMedia(PLAYER_ID, CONTAINER_ID, song.getArtist() + " - " + song.getName(), options.getJavaScriptObject());

			setPosition(0.0);
		}

		state = State.INACTIVE;
	}

	@Override
	public void start() {
		doStart(PLAYER_ID);
	}

	@Override
	public void pause() {
		doPause(PLAYER_ID);
	}

	@Override
	public State getState() {
		return state;
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

		var instance = this;

		aOptions.volumechange = function(event) {
			instance.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onVolumeChange(F)(event.jPlayer.options.volume);
		};
		aOptions.timeupdate = function(event) {
			instance.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPositionChange(F)(event.jPlayer.status.currentTime);
		};
		aOptions.play = function(event) {
			instance.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPlay()();
		};
		aOptions.pause = function(event) {
			instance.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onPause()();
		};
		aOptions.ended = function(event) {
			instance.@net.dorokhov.pony.web.client.mvp.common.PlayerView::onEnd()();
		};

		$wnd.$("#" + aPlayerId).jPlayer(aOptions);

	}-*/;

	private native void updateMedia(String aPlayerId, String aSkinId, String aName, JavaScriptObject aOptions) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("setMedia", aOptions);
		$wnd.$("#" + aSkinId + " .jp-title ul li").text(aName);
	}-*/;

	private native void updateVolume(String aPlayerId, double aVolume) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("volume", aVolume);
	}-*/;

	private native void updatePosition(String aPlayerId, double aPosition) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("play", aPosition);
	}-*/;

	public native void doStart(String aPlayerId) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("play");
	}-*/;

	public native void doPause(String aPlayerId) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("pause");
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

		getUiHandlers().onStart();
	}

	private void onPause() {

		state = State.PAUSED;

		getUiHandlers().onPause();
	}

	private void onEnd() {

		state = State.INACTIVE;

		getUiHandlers().onEnd();
	}

}
