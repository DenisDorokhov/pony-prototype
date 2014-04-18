package net.dorokhov.pony.web.client.mvp.common;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SearchPresenter extends PresenterWidget<SearchPresenter.MyView> {

	public interface MyView extends View {}

	@Inject
	public SearchPresenter(EventBus aEventBus, MyView aView) {
		super(aEventBus, aView);
	}
}
