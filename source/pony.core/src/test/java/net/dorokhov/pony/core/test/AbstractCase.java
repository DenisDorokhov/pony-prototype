package net.dorokhov.pony.core.test;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class AbstractCase {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Before
	public void baseSetUp() throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	@After
	public void baseTearDown() throws Exception {

	}

}
