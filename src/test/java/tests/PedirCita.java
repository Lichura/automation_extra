package tests;

import com.automation.model.testing.SuiteManager;
import com.automation.model.testing.UserStory;
import com.automation.model.utils.CsvToHtml;
import com.automation.model.utils.InitUtils;
import com.project.ProjectConstants;
import com.project.pages.ExtranjeriaHomePage;
import com.project.steps.Steps;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class PedirCita {

    protected SuiteManager suiteM = new SuiteManager(ProjectConstants.EXTRANJERIA);

    // Extranjeria
    @DataProvider(parallel = true)
    public String[][] dataProviderOnlineShop() {
        String testCase = ProjectConstants.EXTRANJERIA;
        String[][] casesMatrix = suiteM.initializeTestObjects(testCase, null, "testDataOnlineShop.csv");

        return casesMatrix;
    }

    @Test(dataProvider = "dataProviderOnlineShop")
    public void onlineShop(String testCase, String id, String browser) throws Exception {
        UserStory userS = InitUtils.createUserStory(id, testCase, suiteM, browser);
        Steps steps = new Steps(userS);

        userS.testActions(() -> {
            steps.user_goes_to("https://sede.administracionespublicas.gob.es/renova2012/");
            steps.user_clicks_on();
            steps.user_fills_form();
            steps.user_accepts_conformidad();
            steps.user_accepts_conformidad();
            steps.user_fills_form_2();
            steps.user_accepts_conformidad();
            ExtranjeriaHomePage page = new ExtranjeriaHomePage(userS);
            String elemento = page.buscarelemento();
            Assert.assertTrue(elemento.contains("nos está disponible en este momento."));
            wait(20);

            return null;
        }).run();
    }



    @AfterSuite
    public void afterSuite() {
        CsvToHtml.createJointReport(suiteM);
    }
}