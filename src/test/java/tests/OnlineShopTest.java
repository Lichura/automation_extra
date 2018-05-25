package tests;

import com.automation.model.testing.SuiteManager;
import com.automation.model.testing.UserStory;
import com.automation.model.testing.objects.TestObject;
import com.automation.model.utils.CsvToHtml;
import com.automation.model.utils.InitUtils;
import com.project.ProjectConstants;
import com.project.steps.Steps;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class OnlineShopTest extends TestObject {

	protected SuiteManager suiteM = new SuiteManager(ProjectConstants.ONLINE_SHOP_CASE);

	// ONLINE_SHOP_CASE
	@DataProvider(parallel = true)
	public String[][] dataProviderOnlineShop() {
		String testCase = ProjectConstants.ONLINE_SHOP_CASE;
		String[][] casesMatrix = suiteM.initializeTestObjects(testCase, null, "testDataOnlineShop.csv");

		return casesMatrix;
	}

	@Test(dataProvider = "dataProviderOnlineShop")
	public void onlineShop(String testCase, String id, String browser) throws Exception {
		UserStory userS = InitUtils.createUserStory(id, testCase, suiteM, browser);
		Steps steps = new Steps(userS);

		userS.testActions(() -> {
			steps.user_goes_to("http://automationpractice.com/index.php");
			steps.user_goes_to_dresses();
			steps.user_choose_a_product_by_color();
			steps.user_adds_product_to_basket();
			steps.user_do_payment();

			return null;
		}).run();
	}

	@AfterSuite
	public void afterSuite() {
		CsvToHtml.createJointReport(suiteM);
	}
}
