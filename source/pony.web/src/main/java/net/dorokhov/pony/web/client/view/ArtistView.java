package net.dorokhov.pony.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import net.dorokhov.pony.web.client.Resources;
import net.dorokhov.pony.web.client.view.event.ArtistViewEvent;
import net.dorokhov.pony.web.shared.ArtistDto;

public class ArtistView extends Composite implements ArtistViewEvent.HasHandler {

	interface MyUiBinder extends UiBinder<Widget, ArtistView> {}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private final HandlerManager handlerManager = new HandlerManager(this);

	private ArtistDto artist;

	private boolean selected;

	@UiField
	FocusPanel artistView;

	@UiField
	Image artistImage;

	@UiField
	Label artistName;

	public ArtistView() {

		Resources.IMPL.cssArtistList().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public ArtistDto getArtist() {
		return artist;
	}

	public void setArtist(ArtistDto aArtist) {

		artist = aArtist;

		updateArtist();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean aSelected) {

		selected = aSelected;

		updateStyle();
	}

	@Override
	public HandlerRegistration addArtistSelectionRequestHandler(ArtistViewEvent.Handler aHandler) {
		return handlerManager.addHandler(ArtistViewEvent.ARTIST_SELECTION_REQUESTED, aHandler);
	}

	@UiHandler("artistView")
	void onArtistViewClick(ClickEvent aEvent) {
		handlerManager.fireEvent(new ArtistViewEvent(ArtistViewEvent.ARTIST_SELECTION_REQUESTED, getArtist()));
	}

	private void updateArtist() {

		if (getArtist() != null && getArtist().getArtworkUrl() != null) {
			if (!artistImage.getUrl().equals(getArtist().getArtworkUrl())) {
				artistImage.setUrl(getArtist().getArtworkUrl());
			}
		} else {
			artistImage.setResource(Resources.IMPL.imgUnknown());
		}

		artistName.setText(getArtist() != null ? getArtist().getName() : null);
	}

	private void updateStyle() {

		artistView.setStyleName(Resources.IMPL.cssArtistList().artistView());

		if (isSelected()) {
			artistView.addStyleName(Resources.IMPL.cssArtistList().artistView_selected());
		}
	}
}
