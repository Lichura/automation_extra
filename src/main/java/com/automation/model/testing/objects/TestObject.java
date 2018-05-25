package com.automation.model.testing.objects;

import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

import com.automation.model.testing.SuiteManager;
import com.automation.model.utils.InitUtils;

public class TestObject {

	protected SuiteManager suiteM;
	protected String translationFile;

	@BeforeSuite
	public synchronized void initializeExecution(ITestContext context) {
		InitUtils.setThreads(context);
	}
}
