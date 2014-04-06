package net.dorokhov.pony.web.client.view.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import net.dorokhov.pony.web.client.presenter.PlayerPresenter;
import net.dorokhov.pony.web.client.view.PlayerView;

public class PlayerViewImpl extends Composite implements PlayerView {

	interface PlayerViewUiBinder extends UiBinder<HTMLPanel, PlayerViewImpl> {}

	private static PlayerViewUiBinder uiBinder = GWT.create(PlayerViewUiBinder.class);

	private PlayerPresenter presenter;

	public PlayerViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setPresenter(PlayerPresenter aPresenter) {
		presenter = aPresenter;
	}

	@Override
	public double getVolume() {
		return 0; // TODO: implement
	}

	@Override
	public void setVolume(double aVolume) {
		// TODO: implement
	}

	@Override
	public double getPosition() {
		return 0; // TODO: implement
	}

	@Override
	public void setPosition(double aPosition) {
		// TODO: implement
	}

	@Override
	public State getState() {
		return null; // TODO: implement
	}

	@Override
	public void setState(State aState) {
		// TODO: implement
	}
}