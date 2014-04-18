package net.dorokhov.pony.web.client.mvp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class ArtistsView extends ViewImpl implements ArtistsPresenter.MyView {

	interface MyUiBinder extends UiBinder<Widget, ArtistsView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
	SimpleLayoutPanel artistListContainer;

	@UiField
	SimpleLayoutPanel albumListContainer;

	public ArtistsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInSlot(Object aSlot, IsWidget aContent) {
		if (aSlot == ArtistsPresenter.SLOT_ARTISTS) {
			artistListContainer.setWidget(aContent);
		} else if (aSlot == ArtistsPresenter.SLOT_ALBUMS) {
			albumListContainer.setWidget(aContent);
		}
	}
}
