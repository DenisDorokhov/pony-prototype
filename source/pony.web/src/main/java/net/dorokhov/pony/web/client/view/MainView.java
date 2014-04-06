package net.dorokhov.pony.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import net.dorokhov.pony.web.client.service.ArtistService;
import net.dorokhov.pony.web.client.service.ArtistServiceAsync;
import net.dorokhov.pony.web.shared.ArtistDto;

import java.util.ArrayList;

public class MainView extends Composite {

	interface MainViewUiBinder extends UiBinder<HTMLPanel, MainView> {}

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	private final HTMLPanel root;

	private final ArtistServiceAsync artistService = GWT.create(ArtistService.class);

	public MainView() {

		root = uiBinder.createAndBindUi(this);

		initWidget(root);

		artistService.getAll(new AsyncCallback<ArrayList<ArtistDto>>() {

			@Override
			public void onFailure(Throwable aThrowable) {
				Window.alert("Error!");
			}

			@Override
			public void onSuccess(ArrayList<ArtistDto> aArtists) {
				for (ArtistDto artist : aArtists) {
					root.add(new HTML(artist.getName()));
				}
			}
		});
	}
}
