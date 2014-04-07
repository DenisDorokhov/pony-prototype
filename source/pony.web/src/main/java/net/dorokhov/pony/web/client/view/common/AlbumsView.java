package net.dorokhov.pony.web.client.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AlbumsView extends Composite {

	interface AlbumsViewUiBinder extends UiBinder<Widget, AlbumsView> {}

	private static AlbumsViewUiBinder uiBinder = GWT.create(AlbumsViewUiBinder.class);

	public AlbumsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
