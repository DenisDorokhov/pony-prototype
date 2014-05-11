package net.dorokhov.pony.web.client.mvp.artists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.view.ArtistCell;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;
import java.util.List;

public class ArtistListView extends ViewWithUiHandlers<ArtistListUiHandlers> implements ArtistListPresenter.MyView {

	interface MyUiBinder extends UiBinder<Widget, ArtistListView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

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

	@UiField(provided = true)
	CellList<ArtistDto> list;

	private ContentState contentState;

	private List<ArtistDto> artists;

	private ListDataProvider<ArtistDto> dataProvider;

	private SingleSelectionModel<ArtistDto> selectionModel;

	public ArtistListView() {

		initList();

		initWidget(uiBinder.createAndBindUi(this));

		scroller.addScrollHandler(new ScrollHandler() {
			@Override
			public void onScroll(ScrollEvent event) {
				GWT.log("!!!scroll!!!!");
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

			int index = list.getVisibleItems().indexOf(selectionModel.getSelectedObject());

			if (index > -1) {
				list.setKeyboardSelectedRow(index); // Scroll to selected item
			}
		}
	}

	private void initList() {

		selectionModel = new SingleSelectionModel<ArtistDto>();

		list = new CellList<ArtistDto>(new ArtistCell());
		list.setSelectionModel(selectionModel);
		list.setVisibleRange(0, Integer.MAX_VALUE);

		dataProvider = new ListDataProvider<ArtistDto>();
		dataProvider.addDataDisplay(list);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent aEvent) {

				ArtistDto artist = selectionModel.getSelectedObject();

				ArtistListView.this.getUiHandlers().onArtistSelection(artist);
			}
		});
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

		if (getArtists() == null) {
			dataProvider.getList().clear();
		} else {

			List<ArtistDto> provider = dataProvider.getList();
			List<ArtistDto> updatedList = getArtists();

			List<Integer> indicesToRemove = new ArrayList<Integer>();

			for (int i = 0; i < provider.size(); i++) {

				ArtistDto artist = provider.get(i);

				if (!updatedList.contains(artist)) {
					indicesToRemove.add(i);
				}
			}

			while (indicesToRemove.size() > 0) {

				int i = indicesToRemove.remove(0);

				provider.remove(i);
			}

			for (int i = 0; i < updatedList.size(); i++) {

				ArtistDto oldArtist = null;
				if (provider.size() > i) {
					oldArtist = provider.get(i);
				}

				ArtistDto newArtist = updatedList.get(i);

				if (newArtist.equals(oldArtist)) {
					provider.set(i, newArtist);
				} else {
					provider.add(i, newArtist);
				}
			}
		}
	}
}
