package net.dorokhov.pony.web.client.mvp.artists;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.client.view.ArtistCell;
import net.dorokhov.pony.web.shared.ArtistDto;

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
	ScrollPanel scroller;

	@UiField(provided = true)
	CellList<ArtistDto> list;

	private ContentState contentState;

	private List<ArtistDto> artists;

	private SingleSelectionModel<ArtistDto> artistListSelectionModel;

	public ArtistListView() {

		initList();

		initWidget(uiBinder.createAndBindUi(this));
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
		return artistListSelectionModel.getSelectedObject();
	}

	@Override
	public void setSelectedArtist(ArtistDto aArtist, boolean aShouldScroll) {

		artistListSelectionModel.setSelected(aArtist, true);

		if (aShouldScroll && artists != null) {

			int index = list.getVisibleItems().indexOf(artistListSelectionModel.getSelectedObject());

			if (index > -1) {
				list.setKeyboardSelectedRow(index); // Scroll to selected item
			}
		}
	}

	private void initList() {

		artistListSelectionModel = new SingleSelectionModel<ArtistDto>();

		list = new CellList<ArtistDto>(new ArtistCell());
		list.setSelectionModel(artistListSelectionModel);

		artistListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent aEvent) {

				ArtistDto artist = artistListSelectionModel.getSelectedObject();

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
					deck.showWidget(scroller);
					break;

				default:
					deck.showWidget(errorLabel);
					break;
			}
		}
	}

	private void updateArtists() {
		list.setRowData(getArtists());
	}
}
