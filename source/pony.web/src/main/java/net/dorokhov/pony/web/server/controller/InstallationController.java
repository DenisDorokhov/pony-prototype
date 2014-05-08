package net.dorokhov.pony.web.server.controller;

import net.dorokhov.pony.web.server.service.InstallationServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class InstallationController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private InstallationServiceFacade installationServiceFacade;

	@Autowired
	public void setInstallationServiceFacade(InstallationServiceFacade aInstallationServiceFacade) {
		installationServiceFacade = aInstallationServiceFacade;
	}

	@RequestMapping("/install")
	synchronized public String install(HttpServletRequest aRequest) {
		
		if (installationServiceFacade.getInstallation() == null) {
			installationServiceFacade.install();
		} else {
			log.warn("Already installed.");
		}

		String redirectPath = "/";
		if (aRequest.getQueryString() != null) {
			redirectPath += "?" + aRequest.getQueryString();
		}
		
		return "redirect:" + redirectPath;
	}
}
