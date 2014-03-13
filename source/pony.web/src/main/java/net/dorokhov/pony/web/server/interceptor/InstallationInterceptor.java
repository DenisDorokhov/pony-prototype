package net.dorokhov.pony.web.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.dorokhov.pony.core.service.InstallationService;
import net.dorokhov.pony.web.server.controller.InstallationController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class InstallationInterceptor extends HandlerInterceptorAdapter {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private InstallationService installationService;

	@Autowired
	public void setInstallationService(InstallationService aInstallationService) {
		installationService = aInstallationService;
	}

	@Override
	public boolean preHandle(HttpServletRequest aRequest, HttpServletResponse aResponse, Object aHandler) throws Exception {
		
		if (aHandler instanceof HandlerMethod) {
			
			HandlerMethod handlerMethod = (HandlerMethod)aHandler;
			
			if (!(handlerMethod.getBean() instanceof InstallationController)) {
				if (installationService.getInstallation() == null) {
					
					log.info("Redirecting to installation...");
					
					aResponse.sendRedirect(aRequest.getContextPath() + "/install");
					
					return false;
				}
			}
		}
		
		return true;
	}
}
