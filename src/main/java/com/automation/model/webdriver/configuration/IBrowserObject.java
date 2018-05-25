package com.automation.model.webdriver.configuration;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

public interface IBrowserObject {

	public WebDriver createWebDriverAndStartBrowser() throws IOException;
}
