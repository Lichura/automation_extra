package com.automation.model.testing.objects;

import java.text.SimpleDateFormat;

import com.automation.data.DataObject;
import com.automation.model.testing.TestDataManager;
import com.automation.model.testing.UserStory;
import com.automation.model.webdriver.DriverHelper;

public class StepObject {

	protected UserStory userS;
	protected DriverHelper webDriver;
	protected TestDataManager testDataM;

	public StepObject(DriverHelper driver) {
		this.webDriver = driver;
	}

	public StepObject(UserStory userStory) {
		this.userS = userStory;
		this.testDataM = userS.getTestDataManager();
		this.webDriver = userS.getDriver();
	}
	
	// Get data methods
	protected DataObject getData(String key) {
		return testDataM.getData(key);
	}
	
	protected String getConfigVar(String key) {
		return testDataM.getConfigVar(key);
	}
	
	protected String getGlobalVar(String key) {
		return testDataM.getGlobalVar(key);
	}
	
	protected String getScenarioVar(String key) {
		String result = null;
		
		if(userS != null && userS.getScenario() != null) {
			result = testDataM.getScenarioVar(userS.getScenario(), key);
		}
		
		return result;
	}
	
	protected String getTestVar(String key) {
		return testDataM.getTestVar(webDriver.getId(), key);
	}
	
	protected String getVar(String rowKey, String key) {
		return testDataM.getVar(rowKey, key);
	}

	// Set data methods
	protected void setData(DataObject data, String key) {
		testDataM.addData(data, key);
	}
	
	protected void setConfigVar(String key, String value) {
		testDataM.setConfigVar(key, value);
	}
	
	protected void setGlobalVar(String key, String value) {
		testDataM.setGlobalVar(key, value);
	}
	
	protected void setScenarioVar(String key, String value) {
		testDataM.setScenarioVar(userS.getScenario(), key, value);
	}
	
	protected void setTestVar(String key, String value) {
		testDataM.setTestVar(webDriver.getId(), key, value);
	}

	// Print to console methods
	private String getDebugLine() {
		int line = Thread.currentThread().getStackTrace()[3].getLineNumber();
		String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(new java.util.Date());
		String className = Thread.currentThread().getStackTrace()[3].getClassName();
		className = className.contains(".") ? className.substring(className.lastIndexOf(".") + 1) : className;
		
		return timeStamp + " - " + className + ":" + line;
	}
	
	protected void debugBegin() {
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		
		System.out.println(getDebugLine() + " - [BEGIN] (" + userS.getTestId() + ") - " + methodName);
	}
	
	protected void debugEnd() {
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		
		System.out.println(getDebugLine() + " - [END] (" + userS.getTestId() + ") - " + methodName);
	}
	
	protected void debugInfo(String message) {
		System.out.println(getDebugLine() + " - [INFO] (" + userS.getTestId() + ") - " + message);
	}
	
	protected void debugError(String message) {
		System.out.println(getDebugLine() + " - [ERROR] (" + userS.getTestId() + ") - " + message);
	}
}
