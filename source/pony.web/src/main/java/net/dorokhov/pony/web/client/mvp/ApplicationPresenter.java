package net.dorokhov.pony.web.client.mvp;

import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import net.dorokhov.pony.web.client.event.SongEvent;
import net.dorokhov.pony.web.client.mvp.common.LogoPresenter;
import net.dorokhov.pony.web.client.mvp.common.PlayerPresenter;
import net.dorokhov.pony.web.client.mvp.common.ToolbarPresenter;
import net.dorokhov.pony.web.client.service.TitleManager;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> implements SongEvent.Handler {

	@ProxyStandard
	public interface MyProxy extends Proxy<ApplicationPresenter> {}

	public interface MyView extends View {}

	@ContentSlot
	public static final GwtEvent.Type<RevealContentHandler<?>> SLOT_CONTENT = new GwtEvent.Type<RevealContentHandler<?>>();

	public static final Object SLOT_LOGO = new Object();
	public static final Object SLOT_PLAYER = new Object();
	public static final Object SLOT_SEARCH = new Object();

	private final LogoPresenter logoPresenter;
	private final PlayerPresenter playerPresenter;
	private final ToolbarPresenter toolbarPresenter;

	private final TitleManager titleManager;

	@Inject
	public ApplicationPresenter(EventBus aEventBus, MyView aView, MyProxy aProxy,
								LogoPresenter aLogoPresenter, PlayerPresenter aPlayerPresenter, ToolbarPresenter aToolbarPresenter) {

		super(aEventBus, aView, aProxy, RevealType.RootLayout);

		logoPresenter = aLogoPresenter;
		playerPresenter = aPlayerPresenter;
		toolbarPresenter = aToolbarPresenter;

		titleManager = new TitleManager();
	}

	@Override
	protected void onBind() {

		super.onBind();

		setInSlot(SLOT_LOGO, logoPresenter);
		setInSlot(SLOT_PLAYER, playerPresenter);
		setInSlot(SLOT_SEARCH, toolbarPresenter);

		getEventBus().addHandler(SongEvent.SONG_CHANGED, this);
	}

	@Override
	public void onSongEvent(SongEvent aEvent) {
		titleManager.setSong(aEvent.getSong());
	}
}
