package com.project.pages;

import org.openqa.selenium.By;
import com.automation.model.testing.UserStory;
import com.automation.model.testing.objects.PageObject;

public class ShopSelectionPage extends PageObject {
	
	// region WebElements
	private By firstProductBtn = By.cssSelector(".first-in-line .product-image-container");
	private By addToCartBtn = By.cssSelector("#add_to_cart > button");
	private By proceedToCheckOutBtn = By.cssSelector(".button-container [rel='nofollow']");
	private By sizeDrpDwn = By.cssSelector("#group_1");
	private By moreBtn = By.cssSelector(".button-container [itemprop='url']");
	// endregion

	public ShopSelectionPage(UserStory userS) {
		super(userS);
	}

	// region Methods
	public ShopSelectionPage applyColorFilter(String color) {
		debugBegin();
		
		webDriver.click(By.xpath("//*[@class='layered_color']/a[text()='" + color + "']"));
		webDriver.waitForJQuery();
		webDriver.moveToElement(firstProductBtn);
		webDriver.waitWithDriver(2000);
		webDriver.click(moreBtn);

		debugEnd();
		return this;
	}
	
	public ShopSelectionPage addToCart(String size) {
		debugBegin();
		
		webDriver.clickElementFromDropDownByAttribute(sizeDrpDwn, "title", size);
		webDriver.click(addToCartBtn);
		webDriver.click(proceedToCheckOutBtn);

		debugEnd();
		return this;
	}
	// endregion
}
