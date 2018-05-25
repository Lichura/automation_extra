package com.project.pages;

import org.openqa.selenium.By;
import com.automation.model.testing.UserStory;
import com.automation.model.testing.objects.PageObject;

public class ShopPaymentPage extends PageObject {
	
	// region WebElements
	private By continueBtn = By.cssSelector(".cart_navigation .standard-checkout");
	private By emailInput = By.cssSelector("#email");
	private By passwordInput = By.cssSelector("#passwd");
	private By submitBtn = By.cssSelector("#SubmitLogin");
	private By continueAddressBtn = By.cssSelector("button[name='processAddress']");
	private By agreeChckBx = By.cssSelector("input[name='cgv']");
	private By proceedToPayBtn = By.cssSelector(".bankwire");
	private By confirmOrderBtn = By.cssSelector("[type='submit']");
	// endregion

	public ShopPaymentPage(UserStory userS) {
		super(userS);
	}

	// region Methods
	public ShopPaymentPage pay(String user, String pass) {
		debugBegin();
		
		webDriver.click(continueBtn);
		webDriver.appendText(emailInput, user);
		webDriver.appendText(passwordInput, pass);
		webDriver.click(submitBtn);
		webDriver.click(continueAddressBtn);
		webDriver.click(agreeChckBx);
		webDriver.click(continueBtn);
		webDriver.click(proceedToPayBtn);
		webDriver.click(confirmOrderBtn);

		debugEnd();
		return this;
	}
	// endregion
}
