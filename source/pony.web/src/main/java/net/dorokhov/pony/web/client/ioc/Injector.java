package net.dorokhov.pony.web.client.ioc;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import net.dorokhov.pony.web.client.Bootstrap;

@GinModules(Module.class)
public interface Injector extends Ginjector {

	public Bootstrap getBootstrap();

}
