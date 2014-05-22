package net.dorokhov.pony.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.view.event.SongViewEvent;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongListView extends Composite implements SongViewEvent.HasHandler, SelectionChangeEvent.Handler, SongViewEvent.Handler {

	interface MyUiBinder extends UiBinder<Widget, SongListView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final List<SongView> viewCache = new ArrayList<SongView>();

	static {
		for (int i = 0; i < 500; i++) {
			viewCache.add(new SongView());
		}
	}

	private final Map<SongDto, SongView> songToSongView = new HashMap<SongDto, SongView>();

	private final HandlerManager handlerManager = new HandlerManager(this);

	@UiField
	Label captionLabel;

	@UiField
	FlowPanel songsPanel;

	private SetSelectionModel<SongDto> selectionModel;
	private SetSelectionModel<SongDto> activationModel;

	private List<SongDto> songs;

	private String caption;

	private HandlerRegistration selectionRegistration;
	private HandlerRegistration activationRegistration;

	private boolean playing;

	public SongListView() {

		Resources.IMPL.cssAlbumList().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public SetSelectionModel<SongDto> getSelectionModel() {
		return selectionModel;
	}

	public void setSelectionModel(SetSelectionModel<SongDto> aSelectionModel) {

		if (selectionRegistration != null) {
			selectionRegistration.removeHandler();
			selectionRegistration = null;
		}

		selectionModel = aSelectionModel;

		if (selectionModel != null) {
			selectionRegistration = selectionModel.addSelectionChangeHandler(this);
		}

		updateSongViews();
	}

	public SetSelectionModel<SongDto> getActivationModel() {
		return activationModel;
	}

	public void setActivationModel(SetSelectionModel<SongDto> aActivationModel) {

		if (activationRegistration != null) {
			activationRegistration.removeHandler();
			activationRegistration = null;
		}

		activationModel = aActivationModel;

		if (activationModel != null) {
			activationRegistration = activationModel.addSelectionChangeHandler(this);
		}

		updateSongViews();
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean aPlaying) {

		playing = aPlaying;

		updateSongViews();
	}

	public List<SongDto> getSongs() {
		return songs;
	}

	public void setSongs(List<SongDto> aSongs) {

		songs = aSongs;

		updateSongs();
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String aCaption) {

		caption = aCaption;

		updateCaption();
	}

	public void scrollToSong(SongDto aSong) {

		final SongView view = songToSongView.get(aSong);

		if (view != null) {
			Scheduler.get().scheduleFinally(new Command() {
				@Override
				public void execute() {
					view.getElement().scrollIntoView();
				}
			});
		}
	}

	@Override
	public HandlerRegistration addSongSelectionRequestHandler(SongViewEvent.Handler aHandler) {
		return handlerManager.addHandler(SongViewEvent.SONG_SELECTION_REQUESTED, aHandler);
	}

	@Override
	public HandlerRegistration addSongActivationRequestHandler(SongViewEvent.Handler aHandler) {
		return handlerManager.addHandler(SongViewEvent.SONG_ACTIVATION_REQUESTED, aHandler);
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent aEvent) {
		updateSongViews();
	}

	@Override
	public void onSongViewEvent(SongViewEvent aEvent) {
		if (aEvent.getAssociatedType() == SongViewEvent.SONG_SELECTION_REQUESTED) {
			handlerManager.fireEvent(new SongViewEvent(SongViewEvent.SONG_SELECTION_REQUESTED, aEvent.getSong()));
		} else if (aEvent.getAssociatedType() == SongViewEvent.SONG_ACTIVATION_REQUESTED) {
			handlerManager.fireEvent(new SongViewEvent(SongViewEvent.SONG_ACTIVATION_REQUESTED, aEvent.getSong()));
		}
	}

	private void updateCaption() {
		captionLabel.setText(caption);
	}

	private void updateSongs() {

		List<SongDto> songList = getSongs() != null ? getSongs() : new ArrayList<SongDto>();

		while (songsPanel.getWidgetCount() > songList.size()) {

			int i = songsPanel.getWidgetCount() - 1;

			SongView songView = (SongView) songsPanel.getWidget(i);

			songsPanel.remove(i);

			songView.setSong(null);

			viewCache.add(songView);
		}

		songToSongView.clear();

		for (int i = 0; i < songList.size(); i++) {

			SongDto song = songList.get(i);

			SongView songView;

			if (i < songsPanel.getWidgetCount()) {
				songView = (SongView) songsPanel.getWidget(i);
			} else {

				songView = viewCache.size() > 0 ? viewCache.remove(0) : null;

				if (songView == null) {
					songView = new SongView();
				}

				songView.addSongSelectionRequestHandler(this);
				songView.addSongActivationRequestHandler(this);

				songsPanel.add(songView);
			}

			songView.setSong(song);

			songToSongView.put(song, songView);
		}

		updateSongViews();
	}

	private void updateSongViews() {
		for (Map.Entry<SongDto, SongView> entry : songToSongView.entrySet()) {

			if (getSelectionModel() != null) {
				entry.getValue().setSelected(getSelectionModel().isSelected(entry.getKey()));
			}
			if (getActivationModel() != null) {
				entry.getValue().setActivated(getActivationModel().isSelected(entry.getKey()));
			}

			entry.getValue().setPlaying(isPlaying());
		}
	}
}
