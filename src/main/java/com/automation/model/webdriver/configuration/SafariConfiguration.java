package com.automation.model.webdriver.configuration;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

public class SafariConfiguration {

	public static DesiredCapabilities createDesiredCapabilities() {
		DesiredCapabilities capabilities = DesiredCapabilities.safari();
		capabilities.setPlatform(Platform.MAC);
		capabilities.setBrowserName("safari");
		capabilities.setVersion("10");
		
		return capabilities;
	}

	public static SafariOptions createSafariOptions() {
		SafariOptions options = new SafariOptions();
		
		return options;
	}

}
