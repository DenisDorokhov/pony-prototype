package net.dorokhov.pony.web.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import net.dorokhov.pony.web.client.event.SongPlaybackEvent;
import net.dorokhov.pony.web.client.view.PlayerView;

public class PlayerActivity extends AbstractActivity {

	private PlayerView view;

	@Inject
	public void setView(PlayerView aView) {
		view = aView;
	}

	@Override
	public void start(AcceptsOneWidget aPanel, EventBus aEventBus) {

		aEventBus.addHandler(SongPlaybackEvent.PLAYBACK_REQUESTED, new SongPlaybackEvent.Handler() {
			@Override
			public void onSongPlayback(SongPlaybackEvent aEvent) {
				view.setSong(aEvent.getSong());
				view.start();
			}
		});

		aPanel.setWidget(view);
	}
}
