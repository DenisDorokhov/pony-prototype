package net.dorokhov.pony.web.server.service.impl.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public abstract class AbstractClientService extends RemoteServiceServlet {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private ApplicationContext applicationContext;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void init(ServletConfig aConfig) throws ServletException {

		super.init(aConfig);

		applicationContext = WebApplicationContextUtils.getWebApplicationContext(aConfig.getServletContext());

		initWithApplicationContext(applicationContext);
	}

	protected void initWithApplicationContext(ApplicationContext aContext) {
		// Do nothing by default
	}

}
