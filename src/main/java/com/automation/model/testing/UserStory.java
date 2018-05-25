package com.automation.model.testing;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.automation.configuration.AutomationConstants;
import com.automation.data.DataObject;
import com.automation.model.utils.FileUtils;
import com.automation.model.webdriver.DriverHelper;

/**
 * The UserStory class is the base to execute test, having a reference to a TestDataManager, which uses using its String
 * variable "testId" to access the variables specific to a test, and the String variable "scenario" to access the
 * variables specific to a scenario.
 * 
 * It also controls the execution of the test, managing the errors to write them in a result file.
 *
 * @author Alfredo Moises Boullosa Ramones
 */
public class UserStory {

	private int tries = 0;
	private int maxTries = 0;
	private long startTime;
	private long duration = 0;
	private String result = "";
	private String browser;
	private String timeStamp;
	private String reportPath;
	private String lastException = "";
	private String testId = "0";
	private String scenario = "";
	private String[][] resultMatrix;
	private TestDataManager testDataM;
	private DataObject driverConf;
	private DriverHelper webDriver;
	private boolean retryOnFail = false;
	private ArrayList<Callable<Void>> actionList = new ArrayList<Callable<Void>>();
	private ArrayList<Callable<Void>> onFailList = new ArrayList<Callable<Void>>();
	private ArrayList<Callable<Void>> onSuccessList = new ArrayList<Callable<Void>>();
	private ArrayList<Callable<Void>> onEndList = new ArrayList<Callable<Void>>();

	public UserStory(String scenario) {
		this.scenario = scenario;
	}

	public UserStory(String id, String scenario) {
		this.scenario = scenario;
		this.testId = id;
	}

	public UserStory addData(DataObject dataObject, String key) {
		testDataM.addData(dataObject, key);

		return this;
	}

	public UserStory addDMData(String fileName, String key) {
		testDataM.addDMData(fileName, key);

		return this;
	}

	public UserStory addMData(String fileName, String key) {
		testDataM.addMData(fileName, key);

		return this;
	}

	public UserStory addTestDataManager(TestDataManager testData) {
		this.testDataM = testData;

		if(!scenario.isEmpty() && testDataM.getTestVar(testId, "scenario") == null) {
			testDataM.setTestVar(testId, "scenario", scenario);
		}

		return this;
	}

	public UserStory initializeTestDataManager() {
		this.testDataM = new TestDataManager();

		return this;
	}

	public String getTestId() {
		return this.testId;
	}

	public String getScenario() {
		return this.scenario;
	}

	public String getResult() {
		return this.result;
	}

	public String getBrowser() {
		return this.browser;
	}

	public String getLastException() {
		return this.lastException;
	}

	public int getTries() {
		return this.tries;
	}

	public int getMaxTries() {
		return this.maxTries;
	}

	public long getDuration() {
		return this.duration;
	}

	public DriverHelper getDriver() {
		return webDriver;
	}

	public TestDataManager getTestDataManager() {
		return testDataM;
	}

	public DataObject getData(String key) {
		return testDataM.getData(key);
	}

	public String getGlobalVar(String key) {
		return testDataM.getGlobalVar(key);
	}

	public String getScenarioVar(String key) {
		return testDataM.getScenarioVar(scenario, key);
	}

	public String getTestVar(String key) {
		return testDataM.getTestVar(testId, key);
	}

	public String getConfigVar(String key) {
		return testDataM.getConfigVar(key);
	}

	public void setTestVar(String key, String value) {
		testDataM.setTestVar(testId, key, value);
	}
	
	public void setConfigVar(String key, String value) {
		testDataM.setConfigVar(key, value);
	}

	public UserStory setDriver(DriverHelper webDriver) {
		this.webDriver = webDriver;

		return this;
	}

	public UserStory setRetryOnFail(boolean retry) {
		retryOnFail = retry;

		return this;
	}

	public UserStory setScenario(String scenario) {
		this.scenario = scenario;

		return this;
	}

	public UserStory setReportConfiguration(String timeStamp, String path, String[][] resultMatrix) {
		this.reportPath = path;
		this.timeStamp = timeStamp;
		this.resultMatrix = resultMatrix;

		return this;
	}

	public UserStory testActions(Callable<Void> actions) {
		actionList.add(actions);

		return this;
	}

	public UserStory onFail(Callable<Void> actions) {
		onFailList.add(actions);

		return this;
	}

	public UserStory onEnd(Callable<Void> actions) {
		onEndList.add(actions);

		return this;
	}

	private void runFailActions() {
		if(onFailList.size() > 0) {
			System.out.println("[ERROR] (" + testId + ") -" + testDataM.caseVariablesToString(testId) + " browser: " + getBrowser() + ", lastException: " + lastException);

			try {
				System.out.println("[BEGIN] (" + testId + ") - On fail steps");
				for(int i = 0; i < onFailList.size(); i++) {
					onFailList.get(i).call();
				}
				System.out.println("[ END ] (" + testId + ") - On fail steps");

			} catch(Exception e1) {
				System.out.println("[ERROR] (" + testId + ") - " + testId + " ON FAIL ACTIONS");
				e1.printStackTrace();
			}
		}
	}

	private void runSuccessActions() {
		if(onSuccessList.size() > 0) {
			try {
				System.out.println("[BEGIN] (" + testId + ") - On success steps");
				for(int i = 0; i < onSuccessList.size(); i++) {
					onSuccessList.get(i).call();
				}
				System.out.println("[ END ] (" + testId + ") - On success steps");

			} catch(Exception e1) {
				System.out.println("[ERROR] (" + testId + ") - " + testId + " ON SUCCESS ACTIONS");
				e1.printStackTrace();
			}
		}
	}

	private void runEndActions() {
		if(onEndList.size() > 0) {
			try {
				System.out.println("[BEGIN] (" + testId + ") - On end steps");
				for(int i = 0; i < onEndList.size(); i++) {
					onEndList.get(i).call();
				}
				System.out.println("[ END ] (" + testId + ") - On end steps");

			} catch(Exception e1) {
				System.out.println("[ERROR] (" + testId + ") - " + testId + " ON END ACTIONS");
				e1.printStackTrace();
			}
		}
	}

	public Boolean run() throws Exception {
		boolean testResult = false;
		Exception exception = null;
		Error error = null;

		System.out.println("[BEGIN] (" + testId + ") - Test execution");
		startTime = System.currentTimeMillis();

		try {
			try {
				System.out.println("[BEGIN] (" + testId + ") - Test steps");
				System.out.println("[INFO] (" + testId + "/" + (testDataM.getTestData().size() - 1) + ") -" + testDataM.caseVariablesToString(testId) + ", browser: " + getBrowser());

				for(int i = 0; i < actionList.size(); i++) {
					actionList.get(i).call();
				}
				
				System.out.println("[ END ] (" + testId + ") - Test steps");

				System.out.println("[ END ] (" + testId + ") - Test execution ended successfully");
				testResult = true;
			} catch(Exception e) {
				System.out.println("[ERROR] (" + testId + ") - EXCEPTION ON TEST ACTIONS: " + testDataM.caseVariablesToString(testId));
				e.printStackTrace();
				exception = e;

				takeErrorScreenshot();
			} catch(Error e) {
				System.out.println("[ERROR] (" + testId + ") - ERROR ON TEST ACTIONS: " + testDataM.caseVariablesToString(testId));
				e.printStackTrace();
				error = e;

				takeErrorScreenshot();
			}
		} catch(Exception e) {
			System.out.println("[ERROR] (" + testId + ") - [ERROR] Initializing driver");
			e.printStackTrace();
			exception = e;
		}

		webDriver.quit();

		if(!testResult && checkTries(exception, error)) {
			runFailActions();

			return run();
		} else {
			duration = (System.currentTimeMillis() - startTime) / 1000;

			if(!testResult) {
				result = AutomationConstants.TEST_FAILURE;

				runFailActions();
				runEndActions();
				
				saveExceptionIntoFile(exception != null ? ExceptionUtils.getStackTrace(exception) : error.getStackTrace().toString(), testId);
				
				String failure = exception != null ? exception.getMessage() != null ? exception.getMessage() : "" 
					: error.getMessage() != null ? error.getMessage() : "";
					
				System.out.println("[ END ] (" + testId + ") - Test execution ended with failure" + (failure != null && !failure.isEmpty() ? ": " + failure : ""));

				updateResultMatrix();

				if(exception != null) {
					throw exception;					
				} else {
					throw error;
				}
			} else {
				result = AutomationConstants.TEST_SUCCESS;

				runSuccessActions();
				runEndActions();

				updateResultMatrix();

				return testResult;
			}
		}
	}
	
	private boolean setBooleanOnConfiguration(String key) {
		String stringValue = System.getProperty(key);
		if((stringValue != null && stringValue.isEmpty()) || stringValue == null) stringValue = driverConf.getValue(key);
		else if(stringValue != null) driverConf.setValue(key, stringValue);
		
		return Boolean.parseBoolean(stringValue);
	}
	
	private String setStringOnConfiguration(String key) {
		String stringValue = System.getProperty(key);
		if((stringValue != null && stringValue.isEmpty()) || stringValue == null) stringValue = driverConf.getValue(key);
		else if(stringValue != null) driverConf.setValue(key, stringValue);
		
		return stringValue;
	}

	public UserStory addDriverConfiguration(String browser, DataObject conf) {
		System.out.println("[BEGIN] (" + testId + ") - Setting driver configuration");
		driverConf = conf;
		this.browser = browser;
		this.maxTries = Integer.parseInt(driverConf.getValue("max_tries"));

		testDataM.setTestVar(testId, "browser", browser);
		webDriver = new DriverHelper(browser);
		webDriver.setId(testId);
		webDriver.setReportPath(testDataM.getReportPath());

		webDriver.setWaitForAngular(setBooleanOnConfiguration("wait_for_angular"));
		webDriver.setWaitForJQuery(setBooleanOnConfiguration("wait_for_jquery"));

		setBooleanOnConfiguration("driver_type");
		
		webDriver.setEmulationBrowser(setStringOnConfiguration("emulation_browser"));

		webDriver.setDownloadDrivers(setBooleanOnConfiguration("download"));
		webDriver.setHub(setStringOnConfiguration(AutomationConstants.IP), setStringOnConfiguration(AutomationConstants.PORT));
		
		webDriver.setForceCache(setBooleanOnConfiguration("force_cache"));

		webDriver.setRemoteMode(setBooleanOnConfiguration("remote"));
		
		if(conf.getValue(AutomationConstants.SMALL_WINDOW_LIMIT) != null) {
			webDriver.setSmallWindowLimit(Integer.parseInt(conf.getValue(AutomationConstants.SMALL_WINDOW_LIMIT)));
		}

		String timeout = System.getProperty("timeout");
		if(timeout != null && !timeout.isEmpty()) conf.setValue(AutomationConstants.TIMEOUT, timeout);
		if(conf.getValue(AutomationConstants.TIMEOUT) != null) {
			webDriver.setImplicitWait(Integer.parseInt(conf.getValue(AutomationConstants.TIMEOUT)));
			webDriver.setScriptWait(Integer.parseInt(conf.getValue(AutomationConstants.TIMEOUT)));
			webDriver.setPageLoadWait(Integer.parseInt(conf.getValue(AutomationConstants.TIMEOUT)));
		} else {
			webDriver.setImplicitWait(Integer.parseInt(conf.getValue(AutomationConstants.IMPLICIT_WAIT)));
			webDriver.setScriptWait(Integer.parseInt(conf.getValue(AutomationConstants.SCRIPT_WAIT)));
			webDriver.setPageLoadWait(Integer.parseInt(conf.getValue(AutomationConstants.PAGE_LOAD_WAIT)));
		}
		
		if(conf.getValue(AutomationConstants.WINDOW_HEIGTH) != null && conf.getValue(AutomationConstants.WINDOW_WIDTH) != null) {
			webDriver.setWindowSize(Integer.parseInt(conf.getValue(AutomationConstants.WINDOW_HEIGTH)), 
				Integer.parseInt(conf.getValue(AutomationConstants.WINDOW_WIDTH)));
		} 

		retryOnFail = setBooleanOnConfiguration(AutomationConstants.RETRY_ON_FAIL);
		
		System.out.println("[INFO ] (" + testId + ") - browser: " + browser + ", ip: " + conf.getValue(AutomationConstants.IP) + ", port: " 
			+ conf.getValue(AutomationConstants.PORT) + ", remote: " + conf.getValue(AutomationConstants.REMOTE_MODE));
		System.out.println("[ END ] (" + testId + ") - Setting driver configuration");

		return this;
	}

	private synchronized boolean checkTries(Exception exception, Error error) {
		boolean result = false;

		String failure = exception != null ? exception.getClass().getCanonicalName() : error.getClass().getCanonicalName();
		
		System.out.println("[INFO ] (" + testId + ") - Checking retryOnFail: " + retryOnFail
			+ (retryOnFail ? ", max tries: " + maxTries + ", current tries: " + (tries + 1) : "")
			+ ((lastException != null && lastException.equals(failure)) ? ", stopping execution because of same error" : ""));

		if((retryOnFail && lastException != null && !lastException.equals(failure)) && tries < maxTries) {
			tries++;
			System.out.println("[INFO ] (" + testId + ") - Trying again");

			result = true;
		} else if(retryOnFail) {
			System.out.println("[INFO ] (" + testId + ") - Last try, finishing with error");
		}

		lastException = failure;

		return result;
	}

	public synchronized UserStory takeScreenshot() {
		System.out.println("[BEGIN] (" + testId + ") - Taking screenshot");
		try {
			if(timeStamp != null && webDriver != null) {
				new File(reportPath + "/" + AutomationConstants.IMAGES_FOLDER).mkdirs();

				webDriver.takeScreenshot(timeStamp + ".i" + testId, reportPath + "/" + AutomationConstants.IMAGES_FOLDER + "/");
			}
		} catch(Exception e) {
			System.out.println("[ERROR] (" + testId + ") - Taking screenshot");
		}
		System.out.println("[ END ] (" + testId + ") - Taking screenshot");

		return this;
	}

	private synchronized UserStory takeErrorScreenshot() {
		try {
			if(timeStamp != null && webDriver != null) {
				System.out.println("[BEGIN] (" + testId + ") - Taking screenshot: '[ERROR] - " + timeStamp + ".i" + testId + ".jpg'");
				new File(reportPath + "/" + AutomationConstants.IMAGES_FOLDER).mkdirs();

				webDriver.takeScreenshot("[ERROR] - " + timeStamp + ".i" + testId, reportPath + "/" + AutomationConstants.IMAGES_FOLDER);
				System.out.println("[ END ] (" + testId + ") - Taking screenshot");
			}
		} catch(Exception e) {
			System.out.println("[ERROR] (" + testId + ") - Taking screenshot");
		}

		return this;
	}

	public synchronized void saveExceptionIntoFile(String exception, String id) {
		System.out.println("[BEGIN] (" + testId + ") - Saving Exception into file");
		try {
			new File(reportPath + "/" + AutomationConstants.EXCEPTIONS_FOLDER + "/").mkdirs();

			FileUtils.writeFile(reportPath + "/" + AutomationConstants.EXCEPTIONS_FOLDER + "/" + this.timeStamp + ".e" + Integer.parseInt(id) + ".txt", exception);
		} catch(Exception e) {
			System.out.println("[ERROR] (" + testId + ") - Saving Exception into file");
		}
		System.out.println("[ END ] (" + testId + ") - Saving Exception into file");
	}

	private void updateResultMatrix() {
		if(resultMatrix != null) {
			synchronized(resultMatrix) {
				try {
					System.out.println("[BEGIN] (" + testId + ") - Updating result matrix");
					System.out.println("[INFO] (" + testId + "/" + (testDataM.getTestData().size() - 1) + ") -" + testDataM.caseVariablesToString(testId)
						+ ", browser: " + getBrowser() + ", time: " + duration 
						+ (lastException.isEmpty() || result.equals(AutomationConstants.TEST_SUCCESS) ? "" : ", report: " + lastException));

					String[] resultArray = new String[testDataM.getCaseVariables().length + 4];

					for(int i = 0; i < testDataM.getCaseVariables().length; i++) {
						resultArray[i] = getTestVar(testDataM.getCaseVariables()[i]);
					}

					resultArray[resultArray.length - 4] = browser;
					resultArray[resultArray.length - 3] = result;
					resultArray[resultArray.length - 2] = Long.toString(duration);
					resultArray[resultArray.length - 1] = result.equals(AutomationConstants.TEST_SUCCESS) ? "" : lastException;

					resultMatrix[Integer.parseInt(testId) + 1] = resultArray;

					System.out.println("[INFO] (" + testId + ") - Saving results as " + testDataM.getTimeStamp() + ".csv");
					new File(testDataM.getReportPath()).mkdirs();
					FileUtils.writeArrayIntoCSVFile(testDataM.getReportPath() + testDataM.getTimeStamp() + ".csv", resultMatrix);
					System.out.println("[ END ] (" + testId + ") - Updating result matrix");
				} catch(Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}
}