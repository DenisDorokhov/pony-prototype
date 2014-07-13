package net.dorokhov.pony.web.server.interceptor;

import net.dorokhov.pony.web.server.service.InstallationServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InstallationInterceptor extends HandlerInterceptorAdapter {

	private final Object lock = new Object();

	private InstallationServiceFacade installationServiceFacade;

	@Autowired
	public void setInstallationServiceFacade(InstallationServiceFacade aInstallationService) {
		installationServiceFacade = aInstallationService;
	}

	@Override
	public boolean preHandle(HttpServletRequest aRequest, HttpServletResponse aResponse, Object aHandler) throws Exception {

		if (aHandler instanceof HandlerMethod && installationServiceFacade.getInstallation() == null) {
			synchronized (lock) {
				if (installationServiceFacade.getInstallation() == null) {
					installationServiceFacade.install();
				}
			}
		}

		return true;
	}
}
