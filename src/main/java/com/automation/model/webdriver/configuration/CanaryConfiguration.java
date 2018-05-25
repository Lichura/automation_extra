package com.automation.model.webdriver.configuration;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class CanaryConfiguration {

	public DesiredCapabilities createDesiredCapabilities(boolean headless) throws IOException {
		DesiredCapabilities desiredCapabilitiesLocal = DesiredCapabilities.chrome();
		desiredCapabilitiesLocal.setCapability(ChromeOptions.CAPABILITY, CanaryConfiguration.createChromeOptions(headless));
		
		return desiredCapabilitiesLocal;
	}

	public static ChromeOptions createChromeOptions(boolean headless) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-popup-blocking");
		
		if(headless) {
			options.addArguments("-headless");
			options.addArguments("--disable-gpu");
		}
		
		options.addArguments("--start-maximized");
		Map<String, Object> preferences = new Hashtable<>();
		options.setExperimentalOption("prefs", preferences);

		// disable flash and the PDF viewer
		preferences.put("plugins.plugins_disabled", new String[]{ "Chrome PDF Viewer"});
		
		return options;
	}
}
