package com.automation.model.webdriver.configuration;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class EdgeConfiguration {

	public Proxy createProxy() {
		Proxy proxy = new Proxy();
		proxy.setProxyType(ProxyType.SYSTEM);
		
		return proxy;
	}

	public static DesiredCapabilities createDesiredCapabilities() {
		DesiredCapabilities cap = DesiredCapabilities.edge();
		
		return cap;
	}

	public static EdgeOptions createEdgeOptions() {
		EdgeOptions options = new EdgeOptions();
		options.setPageLoadStrategy("disable-popup-blocking");
		options.setPageLoadStrategy("--start-maximized");
		
		return options;
	}

}
