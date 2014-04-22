package net.dorokhov.pony.web.server.service.impl.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public abstract class AbstractServiceRpcServlet extends RemoteServiceServlet {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private WebApplicationContext applicationContext;

	public WebApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void init(ServletConfig aConfig) throws ServletException {

		super.init(aConfig);

		applicationContext = WebApplicationContextUtils.getWebApplicationContext(aConfig.getServletContext());

		initWithApplicationContext(applicationContext);
	}

	protected void initWithApplicationContext(WebApplicationContext aContext) {
		// Do nothing by default
	}

}
