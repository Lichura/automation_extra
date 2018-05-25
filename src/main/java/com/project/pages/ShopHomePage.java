package com.project.pages;

import org.openqa.selenium.By;
import com.automation.model.testing.UserStory;
import com.automation.model.testing.objects.PageObject;

public class ShopHomePage extends PageObject {
	
	// region WebElements
	private By dressesBtn = By.cssSelector(".sf-menu > li:nth-child(2)");
	// endregion

	public ShopHomePage(UserStory userS) {
		super(userS);
	}

	// region Methods
	public ShopHomePage goToDresses(String dresses) {
		debugBegin();
		
		webDriver.moveToElement(dressesBtn);		
		webDriver.click(By.cssSelector(".sf-menu > li:nth-child(2) [title^='" + dresses + "']"));

		debugEnd();

		return this;
	}
	// endregion
}
