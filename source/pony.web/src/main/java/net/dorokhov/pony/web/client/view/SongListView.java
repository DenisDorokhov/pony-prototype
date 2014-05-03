package net.dorokhov.pony.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.view.event.SongRequestEvent;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.*;

public class SongListView extends Composite implements SongRequestEvent.HasHandler, SelectionChangeEvent.Handler, SongRequestEvent.Handler {

	interface SongListUiBinder extends UiBinder<Widget, SongListView> {}

	private static final SongListUiBinder uiBinder = GWT.create(SongListUiBinder.class);

	private static final List<SongView> viewCache = new ArrayList<SongView>();

	static {
		for (int i = 0; i < 500; i++) {
			viewCache.add(new SongView());
		}
	}

	private final HandlerManager handlerManager = new HandlerManager(this);

	private final List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

	private final HashMap<Long, SongView> songToSongView = new HashMap<Long, SongView>();

	@UiField
	Label captionLabel;

	@UiField
	FlowPanel songListView;

	private SetSelectionModel<SongDto> selectionModel;
	private SetSelectionModel<SongDto> activationModel;

	private List<SongDto> songs;

	private String caption;

	private HandlerRegistration selectionRegistration;
	private HandlerRegistration activationRegistration;

	public SongListView() {

		Resources.IMPL.style().ensureInjected();

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

		updateSelection();
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

		updateSelection();
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

	@Override
	public HandlerRegistration addSongSelectionRequestHandler(SongRequestEvent.Handler aHandler) {
		return handlerManager.addHandler(SongRequestEvent.SONG_SELECTION_REQUESTED, aHandler);
	}

	@Override
	public HandlerRegistration addSongActivationRequestHandler(SongRequestEvent.Handler aHandler) {
		return handlerManager.addHandler(SongRequestEvent.SONG_ACTIVATION_REQUESTED, aHandler);
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent aEvent) {
		updateSelection();
	}

	@Override
	public void onSongRequest(SongRequestEvent aEvent) {
		if (aEvent.getAssociatedType() == SongRequestEvent.SONG_SELECTION_REQUESTED) {
			handlerManager.fireEvent(new SongRequestEvent(SongRequestEvent.SONG_SELECTION_REQUESTED, aEvent.getSong()));
		} else if (aEvent.getAssociatedType() == SongRequestEvent.SONG_ACTIVATION_REQUESTED) {
			handlerManager.fireEvent(new SongRequestEvent(SongRequestEvent.SONG_ACTIVATION_REQUESTED, aEvent.getSong()));
		}
	}

	private void updateCaption() {
		captionLabel.setText(caption);
	}

	private void updateSongs() {

		while (songListView.getWidgetCount() > 0) {

			Widget widget = songListView.getWidget(0);

			songListView.remove(0);

			if (widget instanceof SongView) {

				SongView songListView = (SongView) widget;

				songListView.setSong(null);

				viewCache.add(songListView);
			}
		}

		for (HandlerRegistration registration : handlerRegistrations) {
			registration.removeHandler();
		}

		handlerRegistrations.clear();

		songToSongView.clear();

		if (songs != null) {
			for (SongDto song : songs) {

				SongView songView = viewCache.size() > 0 ? viewCache.remove(0) : null;

				if (songView == null) {
					songView = new SongView();
				}

				songView.setSong(song);

				handlerRegistrations.add(songView.addSongSelectionRequestHandler(this));
				handlerRegistrations.add(songView.addSongActivationRequestHandler(this));

				songToSongView.put(song.getId(), songView);
				songListView.add(songView);
			}
		}

		updateSelection();
	}

	private void updateSelection() {

		Set<SongDto> selectedSongs = null;
		Set<SongDto> activatedSongs = null;

		if (getSelectionModel() != null) {
			selectedSongs = getSelectionModel().getSelectedSet();
		}
		if (getActivationModel() != null) {
			activatedSongs = getActivationModel().getSelectedSet();
		}

		for (Map.Entry<Long, SongView> entry : songToSongView.entrySet()) {
			if (selectedSongs != null) {
				entry.getValue().setSelected(selectedSongs.contains(entry.getValue().getSong()));
			}
			if (activatedSongs != null) {
				entry.getValue().setActivated(activatedSongs.contains(entry.getValue().getSong()));
			}
		}
	}
}
