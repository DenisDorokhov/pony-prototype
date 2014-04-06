package net.dorokhov.pony.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import net.dorokhov.pony.web.client.ioc.Injector;

public class Application implements EntryPoint {

	private final Injector injector = GWT.create(Injector.class);

	@Override
	public void onModuleLoad() {

		Bootstrap bootstrap = injector.getBootstrap();

		bootstrap.run();
	}
}
