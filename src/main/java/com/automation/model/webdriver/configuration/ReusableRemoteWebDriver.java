package com.automation.model.webdriver.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ReusableRemoteWebDriver extends RemoteWebDriver {

	private String sessionId;

	public ReusableRemoteWebDriver(URL url, Capabilities capabilities) {
		super(url, capabilities);
	}

	public ReusableRemoteWebDriver(DesiredCapabilities desiredCapabilities) {
		this.startSession(desiredCapabilities);
	}

	public ReusableRemoteWebDriver(String sessionId, URL remoteUrl, DesiredCapabilities capabilities) {
		HttpCommandExecutor executor = new HttpCommandExecutor(remoteUrl);
		this.sessionId = sessionId;
		this.setSessionId(sessionId);
		this.setCommandExecutor(executor);
		// this.init(this.desiredCapabilities, null);
		
		//this.startClient();
		this.startSession(capabilities);
	}

	public void createRemoteDriverService() throws IOException {
		// create new ChromeDriverService
		ChromeDriverService service = new ChromeDriverService.Builder()
			.usingDriverExecutable(new File("chrome/chromedriver")).usingAnyFreePort().build();
		
		service.start();
	}

	@Override
	protected void startSession(Capabilities desiredCapabilities) {
		String sid = sessionId;

		if(sid != null) {
			this.setSessionId(sid);
			try {
				this.getCurrentUrl();
			} catch(WebDriverException e) {
				// session is not valid
				sid = null;
			}
		}

		if(sid == null) {
			super.startSession(desiredCapabilities);
			// saveSessionIdToSomeStorage(getSessionId().toString());
		}
	}

	// private String getPreviousSessionIdFromSomeStorage() throws IOException
	// {
	// this.configurationProperties = new ConfigurationProperties();
	//
	// return this.configurationProperties.SessionID;
	// }
}
