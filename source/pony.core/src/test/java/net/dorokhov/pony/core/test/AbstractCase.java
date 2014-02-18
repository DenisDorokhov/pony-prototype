package net.dorokhov.pony.core.test;

import org.junit.After;
import org.junit.Before;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class AbstractCase {

	@Before
	public void baseSetUp() throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	@After
	public void baseTearDown() throws Exception {

	}

}
