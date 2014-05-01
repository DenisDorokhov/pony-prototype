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
import net.dorokhov.pony.web.client.view.event.SongActivationEvent;
import net.dorokhov.pony.web.client.view.event.SongRequestEvent;
import net.dorokhov.pony.web.shared.SongDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SongListView extends Composite implements SongActivationEvent.HasHandler, SelectionChangeEvent.Handler, SongRequestEvent.Handler {

	interface SongListUiBinder extends UiBinder<Widget, SongListView> {}

	private static final SongListUiBinder uiBinder = GWT.create(SongListUiBinder.class);

	private final HandlerManager handlerManager = new HandlerManager(this);

	private final List<SongView> songViews = new ArrayList<SongView>();

	@UiField
	Label captionLabel;

	@UiField
	FlowPanel songListView;

	private SetSelectionModel<SongDto> selectionModel;

	private ArrayList<SongDto> songs;

	private String caption;

	private HandlerRegistration selectionRegistration;

	public SongListView() {

		Resources.INSTANCE.style().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public SongListView(SetSelectionModel<SongDto> aSelectionModel) {

		this();

		setSelectionModel(aSelectionModel);
	}

	public SongListView(SetSelectionModel<SongDto> aSelectionModel, ArrayList<SongDto> aSongs) {

		this(aSelectionModel);

		setSongs(aSongs);
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
	}

	public ArrayList<SongDto> getSongs() {
		return songs;
	}

	public void setSongs(ArrayList<SongDto> aSongs) {

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
	public HandlerRegistration addSongActivationHandler(SongActivationEvent.Handler aHandler) {
		return handlerManager.addHandler(SongActivationEvent.SONG_ACTIVATED, aHandler);
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent aEvent) {

		Set<SongDto> selectedSongs = getSelectionModel().getSelectedSet();

		for (SongView songView : songViews) {
			songView.setSelected(selectedSongs.contains(songView.getSong()));
		}
	}

	@Override
	public void onSongRequest(SongRequestEvent aEvent) {
		if (aEvent.getAssociatedType() == SongRequestEvent.SONG_SELECTION_REQUESTED) {
			getSelectionModel().setSelected(aEvent.getSong(), true);
		} else if (aEvent.getAssociatedType() == SongRequestEvent.SONG_ACTIVATION_REQUESTED) {
			handlerManager.fireEvent(new SongActivationEvent(SongActivationEvent.SONG_ACTIVATED, aEvent.getSong()));
		}
	}

	private void updateCaption() {
		captionLabel.setText(caption);
	}

	private void updateSongs() {

		songViews.clear();
		songListView.clear();

        for (SongDto song : songs) {

            SongView songView = new SongView(song);

			songView.addSongSelectionRequestHandler(this);
			songView.addSongActivationRequestHandler(this);

			songViews.add(songView);
            songListView.add(songView);
        }
	}
}
