package net.dorokhov.pony.web.server.controller;

import net.dorokhov.pony.core.service.InstallationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class InstallationController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private InstallationService installationService;

	@Autowired
	public void setInstallationService(InstallationService aInstallationService) {
		installationService = aInstallationService;
	}

	@RequestMapping("/install")
	synchronized public String install(HttpServletRequest aRequest) {
		
		if (installationService.getInstallation() == null) {
			installationService.install();
		} else {
			log.warn("Already installed.");
		}

		String redirectPath = "/";
		if (aRequest.getQueryString().length() > 0) {
			redirectPath += "?" + aRequest.getQueryString();
		}
		
		return "redirect:" + redirectPath;
	}
}
