package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.common.ObjectUtils;
import net.dorokhov.pony.web.client.common.StringUtils;
import net.dorokhov.pony.web.shared.SongDto;

public class SongView extends Composite {

	interface SongListItemUiBinder extends UiBinder<Widget, SongView> {}

	private static SongListItemUiBinder uiBinder = GWT.create(SongListItemUiBinder.class);

	@UiField
	FocusPanel songViewPanel;

	@UiField
	Label trackNumberLabel;

	@UiField
	Label songNameLabel;

	@UiField
	Label songDurationLabel;

	private SongDto song;

	private SongDelegate delegate;

	public SongView() {
		this(null);
	}

	public SongView(SongDto aSong) {

		Resources.INSTANCE.style().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));

		songViewPanel.sinkEvents(Event.ONCLICK);

		song = aSong;

		updateWidget();
	}

	public void setSong(SongDto aSong) {
		song = aSong;

		updateWidget();
	}

	public SongDto getSong() {
		return song;
	}

	public SongDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SongDelegate aDelegate) {
		delegate = aDelegate;
	}

	@UiHandler("songViewPanel")
	void onSongViewClick(ClickEvent aEvent) {

		songViewPanel.addStyleName(Resources.INSTANCE.style().songListItem_selected()); // TODO: handle selection with selection model

		if (getDelegate() != null) {
			getDelegate().onSongSelected(getSong());
		}
	}

	@UiHandler("songViewPanel")
	void onSongViewDoubleClick(DoubleClickEvent aEvent) {
		if (getDelegate() != null) {
			getDelegate().onSongPlaybackRequested(getSong());
		}
	}

	private void updateWidget() {
		if (song != null) {
			trackNumberLabel.setText(ObjectUtils.nullSafeToString(song.getTrackNumber()));
			songNameLabel.setText(song.getName());
			songDurationLabel.setText(StringUtils.secondsToMinutes(song.getDuration()));
		} else {
			trackNumberLabel.setText(null);
			songNameLabel.setText(null);
			songDurationLabel.setText(null);
		}
	}
}
