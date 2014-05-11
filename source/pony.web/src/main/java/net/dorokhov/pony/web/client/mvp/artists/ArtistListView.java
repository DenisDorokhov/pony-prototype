package net.dorokhov.pony.web.client.mvp.artists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.view.ArtistView;
import net.dorokhov.pony.web.client.view.event.ArtistRequestEvent;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistListView extends ViewWithUiHandlers<ArtistListUiHandlers> implements ArtistListPresenter.MyView, ArtistRequestEvent.Handler {

	interface MyUiBinder extends UiBinder<Widget, ArtistListView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final List<ArtistView> viewCache = new ArrayList<ArtistView>();

	static {
		for (int i = 0; i < 30; i++) {
			viewCache.add(new ArtistView());
		}
	}

	private final Map<ArtistDto, ArtistView> artistToArtistView = new HashMap<ArtistDto, ArtistView>();

	private final List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

	@UiField
	DeckLayoutPanel deck;

	@UiField
	Label loadingLabel;

	@UiField
	Label errorLabel;

	@UiField
	Label noDataLabel;

	@UiField
	ScrollPanel scroller;

	@UiField
	FlowPanel artistsPanel;

	private ContentState contentState;

	private List<ArtistDto> artists;

	private SingleSelectionModel<ArtistDto> selectionModel;

	public ArtistListView() {

		initWidget(uiBinder.createAndBindUi(this));

		selectionModel = new SingleSelectionModel<ArtistDto>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent aEvent) {

				updateArtistViews();

				ArtistDto artist = selectionModel.getSelectedObject();

				ArtistListView.this.getUiHandlers().onArtistSelection(artist);
			}
		});
	}

	public ContentState getContentState() {
		return contentState;
	}

	public void setContentState(ContentState aContentState) {

		contentState = aContentState;

		updateContentState();
	}

	@Override
	public List<ArtistDto> getArtists() {
		return artists;
	}

	@Override
	public void setArtists(List<ArtistDto> aArtists) {

		artists = aArtists;

		updateArtists();
	}

	@Override
	public ArtistDto getSelectedArtist() {
		return selectionModel.getSelectedObject();
	}

	@Override
	public void setSelectedArtist(ArtistDto aArtist, boolean aShouldScroll) {

		selectionModel.setSelected(aArtist, true);

		if (aShouldScroll && artists != null) {

			final ArtistView artistView = artistToArtistView.get(aArtist);

			Scheduler.get().scheduleDeferred(new Command() {
				@Override
				public void execute() {
					artistView.getElement().scrollIntoView();
				}
			});
		}
	}

	@Override
	public void onArtistRequest(ArtistRequestEvent aEvent) {
		selectionModel.setSelected(aEvent.getArtist(), true);
	}

	private void updateContentState() {
		if (getContentState() == null) {

			deck.setVisible(false);

		} else {

			deck.setVisible(true);

			switch (getContentState()) {

				case LOADING:
					deck.showWidget(loadingLabel);
					break;

				case LOADED:
					if (getArtists() == null || getArtists().size() == 0) {
						deck.showWidget(noDataLabel);
					} else {
						deck.showWidget(scroller);
					}
					break;

				default:
					deck.showWidget(errorLabel);
					break;
			}
		}
	}

	private void updateArtists() {

		while (artistsPanel.getWidgetCount() > 0) {

			Widget widget = artistsPanel.getWidget(0);

			artistsPanel.remove(0);

			if (widget instanceof ArtistView) {

				ArtistView albumView = (ArtistView) widget;

				albumView.setArtist(null);

				viewCache.add(albumView);
			}
		}

		for (HandlerRegistration registration : handlerRegistrations) {
			registration.removeHandler();
		}

		handlerRegistrations.clear();
		artistToArtistView.clear();

		if (artists != null) {

			for (ArtistDto artist : artists) {

				ArtistView artistView = viewCache.size() > 0 ? viewCache.remove(0) : null;

				if (artistView == null) {
					artistView = new ArtistView();
				}

				artistView.setArtist(artist);

				handlerRegistrations.add(artistView.addArtistSelectionRequestHandler(this));

				artistToArtistView.put(artist, artistView);
				artistsPanel.add(artistView);
			}
		}

		updateArtistViews();
	}

	private void updateArtistViews() {
		for (Map.Entry<ArtistDto, ArtistView> entry : artistToArtistView.entrySet()) {
			entry.getValue().setSelected(selectionModel.isSelected(entry.getKey()));
		}
	}
}
