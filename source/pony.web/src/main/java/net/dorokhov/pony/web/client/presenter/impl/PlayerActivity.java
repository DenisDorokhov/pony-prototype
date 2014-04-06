package net.dorokhov.pony.web.client.presenter.impl;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.presenter.PlayerPresenter;
import net.dorokhov.pony.web.client.view.PlayerView;

public class PlayerActivity extends AbstractActivity implements PlayerPresenter {

	private PlayerView view;

	@Inject
	public void setView(PlayerView aView) {
		view = aView;
	}

	@Override
	public void start(AcceptsOneWidget aPanel, EventBus aEventBus) {

		view.setPresenter(this);

		aPanel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onVolumeChange(double aVolume) {
		// TODO: implement
	}

	@Override
	public void onPositionChange(double aTime) {
		// TODO: implement
	}

	@Override
	public void onStart() {
		// TODO: implement
	}

	@Override
	public void onPause() {
		// TODO: implement
	}

	@Override
	public void onResume() {
		// TODO: implement
	}
}
