package com.project.steps;

import com.automation.model.testing.UserStory;
import com.automation.model.testing.objects.StepObject;
import com.project.pages.ExtranjeriaHomePage;
import com.project.pages.ShopHomePage;
import com.project.pages.ShopPaymentPage;
import com.project.pages.ShopSelectionPage;

public class Steps extends StepObject {

	public Steps(UserStory userStory) {
		super(userStory);
	}
	
	public void user_goes_to(String url) {
		webDriver.go(url);
	}

	public void user_goes_to_dresses() {
		new ShopHomePage(userS)
			.goToDresses(getTestVar("dress_type"));
	}

	public void user_choose_a_product_by_color() {
		new ShopSelectionPage(userS)
			.applyColorFilter(getTestVar("color"));
	}

	public void user_adds_product_to_basket() {
		new ShopSelectionPage(userS)
			.addToCart(getTestVar("size"));
	}

	public void user_do_payment() {
		new ShopPaymentPage(userS)
			.pay("demoemail@demo.com", "12345");
	}

	public void user_clicks_on() {
		new ExtranjeriaHomePage(userS)
				.consultarConFormulario();
	}

	public void user_fills_form() {
		new ExtranjeriaHomePage(userS)
				.rellenarConsulta();
	}

	public void user_accepts_conformidad() {
		new ExtranjeriaHomePage(userS)
				.aceptarConformidad();
	}


	public void user_fills_form_2() throws InterruptedException {
		new ExtranjeriaHomePage(userS)
				.rellenarDatos();
	}

}
