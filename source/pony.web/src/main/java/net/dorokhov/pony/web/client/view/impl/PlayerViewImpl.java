package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.view.PlayerView;
import net.dorokhov.pony.web.shared.SongDto;

public class PlayerViewImpl extends Widget implements PlayerView {

	private static final String PLAYER_DIV_ID = "jquery_jplayer_1";
	private static final String SKIN_DIV_ID = "jp_container_1";

	private State state;

	private Delegate delegate;

	private double volume = 1.0;

	private double position = 0.0;

	private SongDto song;

	public PlayerViewImpl() {

		setElement(Document.get().createDivElement());

		getElement().appendChild(createPlayer());
		getElement().appendChild(createSkin());
	}

	@Override
	public double getVolume() {
		return volume;
	}

	@Override
	public void setVolume(double aVolume) {

		volume = aVolume;

		updateVolume(PLAYER_DIV_ID, volume);
	}

	@Override
	public double getPosition() {
		return position;
	}

	@Override
	public void setPosition(double aPosition) {

		position = aPosition;

		updatePosition(PLAYER_DIV_ID, aPosition);
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

			updateMedia(PLAYER_DIV_ID, SKIN_DIV_ID, song.getArtist() + " - " + song.getName(), options.getJavaScriptObject());

			setPosition(0.0);
		}

		state = State.INACTIVE;
	}

	@Override
	public void start() {
		doStart(PLAYER_DIV_ID);
	}

	@Override
	public void pause() {
		doPause(PLAYER_DIV_ID);
	}

	@Override
	public void resume() {
		doResume(PLAYER_DIV_ID);
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public Delegate getDelegate() {
		return delegate;
	}

	@Override
	public void setDelegate(Delegate aDelegate) {
		delegate = aDelegate;
	}

	@Override
	protected void onLoad() {

		super.onLoad();

		initPlayer(PLAYER_DIV_ID, createOptions().getJavaScriptObject());
	}

	private DivElement createPlayer() {

		DivElement player = Document.get().createDivElement();

		player.setId(PLAYER_DIV_ID);

		return player;
	}

	private DivElement createSkin() {

		DivElement skinDivElement = Document.get().createDivElement();

		skinDivElement.setId(SKIN_DIV_ID);
		skinDivElement.setClassName("jp-audio");

		skinDivElement.setInnerHTML(
				"<div class=\"jp-type-single\">\n" +
				"\t<div class=\"jp-gui jp-interface\">\n" +
				"\t\t<ul class=\"jp-controls\">\n" +
				"\t\t\t<li><a href=\"javascript:;\" class=\"jp-play\" tabindex=\"1\">play</a></li>\n" +
				"\t\t\t<li><a href=\"javascript:;\" class=\"jp-pause\" tabindex=\"1\">pause</a></li>\n" +
				"\t\t\t<li><a href=\"javascript:;\" class=\"jp-stop\" tabindex=\"1\">stop</a></li>\n" +
				"\t\t\t<li><a href=\"javascript:;\" class=\"jp-mute\" tabindex=\"1\" title=\"mute\">mute</a></li>\n" +
				"\t\t\t<li><a href=\"javascript:;\" class=\"jp-unmute\" tabindex=\"1\" title=\"unmute\">unmute</a></li>\n" +
				"\t\t\t<li><a href=\"javascript:;\" class=\"jp-volume-max\" tabindex=\"1\" title=\"max volume\">max volume</a></li>\n" +
				"\t\t</ul>\n" +
				"\t\t<div class=\"jp-progress\">\n" +
				"\t\t\t<div class=\"jp-seek-bar\">\n" +
				"\t\t\t\t<div class=\"jp-play-bar\"></div>\n" +
				"\t\t\t</div>\n" +
				"\t\t</div>\n" +
				"\t\t<div class=\"jp-volume-bar\">\n" +
				"\t\t\t<div class=\"jp-volume-bar-value\"></div>\n" +
				"\t\t</div>\n" +
				"\t\t<div class=\"jp-time-holder\">\n" +
				"\t\t\t<div class=\"jp-current-time\"></div>\n" +
				"\t\t\t<div class=\"jp-duration\"></div>\n" +
				"\t\t\t<ul class=\"jp-toggles\">\n" +
				"\t\t\t\t<li><a href=\"javascript:;\" class=\"jp-repeat\" tabindex=\"1\" title=\"repeat\">repeat</a></li>\n" +
				"\t\t\t\t<li><a href=\"javascript:;\" class=\"jp-repeat-off\" tabindex=\"1\" title=\"repeat off\">repeat off</a></li>\n" +
				"\t\t\t</ul>\n" +
				"\t\t</div>\n" +
				"\t</div>\n" +
				"\t<div class=\"jp-title\">\n" +
				"\t\t<ul>\n" +
				"\t\t\t<li>&nbsp;</li>\n" +
				"\t\t</ul>\n" +
				"\t</div>\n" +
				"\t<div class=\"jp-no-solution\">\n" +
				"\t\t<span>Update Required</span>\n" +
				"\t\tTo play the media you will need to either update your browser to a recent version or update your <a href=\"http://get.adobe.com/flashplayer/\" target=\"_blank\">Flash plugin</a>.\n" +
				"\t</div>\n" +
				"</div>");

		return skinDivElement;
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
			instance.@net.dorokhov.pony.web.client.view.impl.PlayerViewImpl::onVolumeChange(F)(event.jPlayer.options.volume);
		};
		aOptions.timeupdate = function(event) {
			instance.@net.dorokhov.pony.web.client.view.impl.PlayerViewImpl::onPositionChange(F)(event.jPlayer.status.currentTime);
		};
		aOptions.play = function(event) {
			instance.@net.dorokhov.pony.web.client.view.impl.PlayerViewImpl::onPlay()();
		};
		aOptions.pause = function(event) {
			instance.@net.dorokhov.pony.web.client.view.impl.PlayerViewImpl::onPause()();
		};
		aOptions.ended = function(event) {
			instance.@net.dorokhov.pony.web.client.view.impl.PlayerViewImpl::onEnd()();
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

	public native void doResume(String aPlayerId) /*-{
		$wnd.$("#" + aPlayerId).jPlayer("play");
	}-*/;

	private void onVolumeChange(float aValue) {

		volume = aValue;

		if (delegate != null) {
			delegate.onVolumeChange(this);
		}
	}

	private void onPositionChange(float aValue) {

		position = aValue;

		if (delegate != null) {
			delegate.onPositionChange(this);
		}
	}

	private void onPlay() {

		state = State.PLAYING;

		if (delegate != null) {
			delegate.onStart(this);
		}
	}

	private void onPause() {

		state = State.PAUSED;

		if (delegate != null) {
			delegate.onPause(this);
		}
	}

	private void onEnd() {

		state = State.INACTIVE;

		if (delegate != null) {
			delegate.onEnd(this);
		}
	}
}
