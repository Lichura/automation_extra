package com.automation.model.webdriver.configuration;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobileConfiguration {
	
	final static Logger logger = LoggerFactory.getLogger(MobileConfiguration.class);

	public static DesiredCapabilities createDesiredCapabilities(String browserType) {
		DesiredCapabilities desiredCapabilitiesLocal = DesiredCapabilities.chrome();

		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", getMobileEmulationMap(browserType));

		desiredCapabilitiesLocal.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		
		return desiredCapabilitiesLocal;
	}

	public static ChromeOptions createChromeMobileOptions(String browserType) {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-popup-blocking");
		options.addArguments("--start-maximized");
		Map<String, Object> preferences = new Hashtable<>();
		options.setExperimentalOption("prefs", preferences);

		preferences.put("plugins.plugins_disabled", new String[]{ "Chrome PDF Viewer"});

		//options.setCapability("mobileEmulation", getMobileEmulationMap(browserType));
		options.setExperimentalOption("mobileEmulation", getMobileEmulationMap(browserType));

		return options;
	}
	
	public static FirefoxOptions createFirefoxMobileOptions(String browserType) {
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--start-maximized");
		options.addArguments("-headless");
		//options.addArguments("--disable-setuid-sandbox");
		//Map<String, Object> preferences = new Hashtable<>();
		
		//FirefoxProfile profile= new FirefoxProfile();
		//.

		return options;
	}
	
	private static Map<String, String> getMobileEmulationMap(String browserType) {
		Map<String, String> mobileEmulation = new HashMap<String, String>();

		switch(browserType) {
			case BrowserType.GALAXYS5:
				logger.debug("MOBILE DEVICE: Galaxy S5");
				mobileEmulation.put("deviceName", "Galaxy S5");
				break;
			case BrowserType.NEXUS5X:
				logger.debug("MOBILE DEVICE: Nexus 5X");
				mobileEmulation.put("deviceName", "Nexus 5X");
				break;
			case BrowserType.NEXUS6P:
				logger.debug("MOBILE DEVICE: Nexus 6P");
				mobileEmulation.put("deviceName", "Nexus 6P");
				break;
			case BrowserType.IPHONE5:
				logger.debug("MOBILE DEVICE: iPhone 5");
				mobileEmulation.put("deviceName", "iPhone 5");
				break;
			case BrowserType.IPHONE6:
				logger.debug("MOBILE DEVICE: iPhone 6");
				mobileEmulation.put("deviceName", "iPhone 6");
				break;
			case BrowserType.IPHONE6PLUS:
				logger.debug("MOBILE DEVICE: iPhone 6 Plus");
				mobileEmulation.put("deviceName", "iPhone 6 Plus");
				break;
			case BrowserType.IPAD:
				logger.debug("MOBILE DEVICE: iPad");
				mobileEmulation.put("deviceName", "iPad");
				break;
			case BrowserType.IPADPRO:
				logger.debug("MOBILE DEVICE: iPad Pro");
				mobileEmulation.put("deviceName", "iPad Pro");
				break;
			default:
				logger.debug("FAILED TO RECOGNIZE PHONE TYPE");
		}
		
		return mobileEmulation;
	}
}
