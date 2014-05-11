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

	interface MyUiBinder extends UiBinder<Widget, SongListView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final List<SongView> viewCache = new ArrayList<SongView>();

	static {
		for (int i = 0; i < 500; i++) {
			viewCache.add(new SongView());
		}
	}

	private final HandlerManager handlerManager = new HandlerManager(this);

	private final List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

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
		updateSongViews();
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

		if (songs != null) {
			for (SongDto song : songs) {

				SongView songView = viewCache.size() > 0 ? viewCache.remove(0) : null;

				if (songView == null) {
					songView = new SongView();
				}

				songView.setSong(song);

				handlerRegistrations.add(songView.addSongSelectionRequestHandler(this));
				handlerRegistrations.add(songView.addSongActivationRequestHandler(this));

				songListView.add(songView);
			}
		}

		updateSongViews();
	}

	private void updateSongViews() {

		for (int i = 0; i < songListView.getWidgetCount(); i++) {

			Widget widget = songListView.getWidget(i);

			if (widget instanceof SongView) {

				SongView songView = (SongView) widget;

				if (getSelectionModel() != null) {
					songView.setSelected(getSelectionModel().isSelected(songView.getSong()));
				}
				if (getActivationModel() != null) {
					songView.setActivated(getActivationModel().isSelected(songView.getSong()));
				}

				songView.setPlaying(isPlaying());
			}
		}
	}
}
