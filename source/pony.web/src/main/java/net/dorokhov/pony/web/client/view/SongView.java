package net.dorokhov.pony.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.common.GuiUtils;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.client.view.event.SongViewEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongView extends Composite implements SongViewEvent.HasHandler {

	interface MyUiBinder extends UiBinder<Widget, SongView> {}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private final HandlerManager handlerManager = new HandlerManager(this);

	@UiField
	FocusPanel songView;

	@UiField
	Label trackNumberLabel;

	@UiField
	Label nameLabel;

	@UiField
	Label durationLabel;

	private SongDto song;

	private boolean selected;

	private boolean activated;

	private boolean playing;

	public SongView() {

		Resources.IMPL.cssAlbumList().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));

		setSelected(false);
		setActivated(false);
		setPlaying(false);
	}

	public void setSong(SongDto aSong) {

		song = aSong;

		updateSong();
	}

	public SongDto getSong() {
		return song;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean aSelected) {

		selected = aSelected;

		updateStyle();
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean aActivated) {

		activated = aActivated;

		updateStyle();
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean aPlaying) {

		playing = aPlaying;

		updateStyle();
	}

	@Override
	public HandlerRegistration addSongSelectionRequestHandler(SongViewEvent.Handler aHandler) {
		return handlerManager.addHandler(SongViewEvent.SONG_SELECTION_REQUESTED, aHandler);
	}

	@Override
	public HandlerRegistration addSongActivationRequestHandler(SongViewEvent.Handler aHandler) {
		return handlerManager.addHandler(SongViewEvent.SONG_ACTIVATION_REQUESTED, aHandler);
	}

	@UiHandler("songView")
	void onSongViewClick(ClickEvent aEvent) {
		handlerManager.fireEvent(new SongViewEvent(SongViewEvent.SONG_SELECTION_REQUESTED, getSong()));
	}

	@UiHandler("songView")
	void onSongViewDoubleClick(DoubleClickEvent aEvent) {

		GuiUtils.clearSelection();

		handlerManager.fireEvent(new SongViewEvent(SongViewEvent.SONG_ACTIVATION_REQUESTED, getSong()));
	}

	private void updateSong() {
		if (song != null) {

			trackNumberLabel.setText(ObjectUtils.nullSafeToString(song.getTrackNumber()));
			nameLabel.setText(song.getName());
			durationLabel.setText(StringUtils.secondsToMinutes(song.getDuration()));

		} else {

			trackNumberLabel.setText(null);
			nameLabel.setText(null);
			durationLabel.setText(null);
		}
	}

	private void updateStyle() {

		songView.setStyleName(Resources.IMPL.cssAlbumList().songView());

		if (isActivated()) {

			songView.addStyleName(Resources.IMPL.cssAlbumList().songView_activated());

			if (!isPlaying()) {
				songView.addStyleName(Resources.IMPL.cssAlbumList().songView_paused());
			}
		}
		if (isSelected()) {
			songView.addStyleName(Resources.IMPL.cssAlbumList().songView_selected());
		}
	}
}
