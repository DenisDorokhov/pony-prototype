package net.dorokhov.pony.web.client.mvp.artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;

public class SongListView extends Composite implements SongDelegate {

	interface SongListUiBinder extends UiBinder<Widget, SongListView> {}

	private static SongListUiBinder uiBinder = GWT.create(SongListUiBinder.class);

	private static final int MIN_SONGS_IN_COLUMN = 5;

	@UiField
	Label songListCaption;

	@UiField
	FlowPanel songListPanel;

	private String caption;

	private ArrayList<SongDto> songs;

	private SongDelegate delegate;

	public SongListView() {
		this(null, null);
	}

	public SongListView(ArrayList<SongDto> aSongs) {
		this(aSongs, null);
	}

	public SongListView(ArrayList<SongDto> aSongs, String aCaption) {

		Resources.INSTANCE.style().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));

		setCaption(aCaption);
		setSongs(aSongs);
	}

	public ArrayList<SongDto> getSongs() {
		return songs;
	}

	public void setSongs(ArrayList<SongDto> aSongs) {

		songs = aSongs;

		updateSongList();
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String aCaption) {

		caption = aCaption;

		updateCaption();
	}

	public SongDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SongDelegate aDelegate) {
		delegate = aDelegate;
	}

	@Override
	public void onSongSelected(SongDto aSong) {
		if (getDelegate() != null) {
			getDelegate().onSongSelected(aSong);
		}
	}

	@Override
	public void onSongPlaybackRequested(SongDto aSong) {
		if (getDelegate() != null) {
			getDelegate().onSongPlaybackRequested(aSong);
		}
	}

	private void updateCaption() {
		songListCaption.setText(caption);
	}

	private void updateSongList() {

		songListPanel.clear();

        for (SongDto song : songs) {

            SongView songView = new SongView(song);

            songView.setDelegate(this);

            songListPanel.add(songView);
        }

	}
}
