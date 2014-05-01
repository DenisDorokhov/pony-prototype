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
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.client.view.event.SongRequestEvent;
import net.dorokhov.pony.web.shared.SongDto;

public class SongView extends Composite implements SongRequestEvent.HasHandler {

	interface SongListItemUiBinder extends UiBinder<Widget, SongView> {}

	private static SongListItemUiBinder uiBinder = GWT.create(SongListItemUiBinder.class);

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

	public SongView() {

		Resources.INSTANCE.style().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));

		setSelected(false);
	}

	public SongView(SongDto aSong) {

		this();

		setSong(aSong);
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

		updateSelection();
	}

	@Override
	public HandlerRegistration addSongSelectionRequestHandler(SongRequestEvent.Handler aHandler) {
		return handlerManager.addHandler(SongRequestEvent.SONG_SELECTION_REQUESTED, aHandler);
	}

	@Override
	public HandlerRegistration addSongActivationRequestHandler(SongRequestEvent.Handler aHandler) {
		return handlerManager.addHandler(SongRequestEvent.SONG_ACTIVATION_REQUESTED, aHandler);
	}

	@UiHandler("songView")
	void onSongViewClick(ClickEvent aEvent) {
		handlerManager.fireEvent(new SongRequestEvent(SongRequestEvent.SONG_SELECTION_REQUESTED, getSong()));
	}

	@UiHandler("songView")
	void onSongViewDoubleClick(DoubleClickEvent aEvent) {
		handlerManager.fireEvent(new SongRequestEvent(SongRequestEvent.SONG_ACTIVATION_REQUESTED, getSong()));
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

	private void updateSelection() {
		if (isSelected()) {
			songView.addStyleName(Resources.INSTANCE.style().songView_selected());
		} else {
			songView.setStyleName(Resources.INSTANCE.style().songView());
		}
	}
}