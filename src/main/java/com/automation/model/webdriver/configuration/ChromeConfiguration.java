package com.automation.model.webdriver.configuration;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.BrowserManager;
import io.github.bonigarcia.wdm.ChromeDriverManager;

public class ChromeConfiguration {
	
	final static Logger logger = LoggerFactory.getLogger(ChromeConfiguration.class);
	
	public static void downloadDriver(boolean forceCache) {
		logger.debug("[BEGIN] - Starting BrowserManager setup");
		BrowserManager manager = ChromeDriverManager.getInstance();
		
		if(manager != null) {
			if(forceCache) manager.forceCache();
			manager.setup();
		}
		
		logger.debug("[ END ] - BrowserManager setup finished");
	}

	public static DesiredCapabilities createDesiredCapabilities(boolean headless) {
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(ChromeOptions.CAPABILITY, createChromeOptions(headless));
		
		//CREACION DEL PROXY
		
		
		//cap.setCapability(CapabilityType.PROXY, value);
		
		/*String proxyIp = "110.164.156.194";
		String proxyPort = "8080";
		
		String proxyAddress = proxyIp + ":" + proxyPort;
		
		Proxy proxy = new Proxy();
		
		proxy.setHttpProxy(proxyAddress)
			.setFtpProxy(proxyAddress)
			.setSslProxy(proxyAddress);
		
		cap.setCapability(CapabilityType.PROXY, proxy);*/
		
		return cap;
	}

	public static ChromeOptions createChromeOptions(boolean headless) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-popup-blocking");
		
		if(headless) {
			options.addArguments("-headless");
			options.addArguments("--disable-gpu");
		}

		//options.addArguments("--start-maximized");
		//Map<String, Object> preferences = new Hashtable<>();
		//options.setExperimentalOption("prefs", preferences);

		//preferences.put("plugins.plugins_disabled", new String[]{ "Chrome PDF Viewer"});

		return options;
	}
}
