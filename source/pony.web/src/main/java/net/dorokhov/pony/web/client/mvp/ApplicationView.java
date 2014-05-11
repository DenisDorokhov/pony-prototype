package net.dorokhov.pony.web.client.mvp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import net.dorokhov.pony.web.client.Resources;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

	interface MyUiBinder extends UiBinder<Widget, ApplicationView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField
    FlowPanel logoContainer;

	@UiField
    FlowPanel playerContainer;

	@UiField
    FlowPanel searchContainer;

	@UiField
	SimpleLayoutPanel contentContainer;

	public ApplicationView() {

		Resources.IMPL.cssGlobal().ensureInjected();
        Resources.IMPL.cssHeader().ensureInjected();

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInSlot(Object aSlot, IsWidget aContent) {
		if (aSlot == ApplicationPresenter.SLOT_LOGO) {
			//logoContainer.add(aContent);
            /* TODO: Remove logo view and logo presenter */
		} else if (aSlot == ApplicationPresenter.SLOT_PLAYER) {
			playerContainer.add(aContent);
		} else if (aSlot == ApplicationPresenter.SLOT_SEARCH) {
			searchContainer.add(aContent);
		} else if (aSlot == ApplicationPresenter.SLOT_CONTENT) {
			contentContainer.setWidget(aContent);
		}
	}
}
