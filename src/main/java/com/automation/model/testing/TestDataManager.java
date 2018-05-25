package com.automation.model.testing;

import java.text.SimpleDateFormat;

import com.automation.configuration.AutomationConstants;
import com.automation.data.DataManagerObject;
import com.automation.data.DataObject;
import com.automation.model.utils.ArrayUtils;
import com.automation.model.utils.FileUtils;
import com.automation.model.utils.InitUtils;

/**
 * The TestDataManager class is used to manage the test data, having
 * test data, scenario data and global data.
 * 
 * It is also possible to add more data specific to a test. 
 *
 * @author Alfredo Moises Boullosa Ramones
 */
public class TestDataManager {

	private String testCase;
	private String dailyCase = "";
	private String timeStamp;
	private String reportPath;
	private String[] caseVariables;
	private DataManagerObject data;

	public TestDataManager() {
		data = new DataManagerObject();
	}
	
	public TestDataManager(DataManagerObject data) {
		this.data = data;
	}

	public TestDataManager(DataObject mainData) {
		data = new DataManagerObject();

		data.addData(AutomationConstants.TEST_DATA, mainData);
	}
	
	public void generateTimeStamp(String testCase) {
		this.testCase = testCase;
		// Time Stamp
		dailyCase = System.getProperty("special_case") == null ? "" : System.getProperty("special_case");

		// If not special case, add time to timeStamp
		timeStamp = new SimpleDateFormat("yyyy.MM.dd" + (dailyCase.isEmpty() ? ".HH.mm.ss" : "")).format(new java.util.Date());

		System.out.println("[INFO] - Execution ID: " + timeStamp);

		// Report Folder
		reportPath = System.getProperty("user.dir") + "/" + AutomationConstants.REPORTS_FOLDER + "T" + timeStamp.replace(".", "") + "/";

		// If special case is not empty, add case and browser to file name
		if(!dailyCase.isEmpty()) {
			timeStamp += "." + testCase + "." + ArrayUtils.arrayToString(InitUtils.getTestBrowsers(), ".");
		}
	}

	// region Getters	
	public String getTestCase() {
		return this.testCase;
	}
	
	public String getDailyCase() {
		return this.dailyCase;
	}
	
	public String getReportPath() {
		return this.reportPath;
	}
	
	public String getTimeStamp() {
		return this.timeStamp;
	}
	
	public String[] getCaseVariables() {
		return caseVariables;
	}
	
	public String caseVariablesToString(String testId) {
		String textInfo = "";
		
		for(int i = 0; i < caseVariables.length; i++) {
			textInfo += " " + caseVariables[i] + ": " + getTestVar(testId, caseVariables[i]);
			if(i != caseVariables.length - 1) {
				textInfo += ",";
			}
		}
		return textInfo;
	}
	
	public DataManagerObject getDataManager() {
		return this.data;
	}

	public DataObject getData(String dataKey) {
		return this.data.getData(dataKey);
	}

	public DataObject getTestData() {
		return this.data.getData(AutomationConstants.TEST_DATA);
	}
	
	public DataObject getConfigData() {
		return data.getData(AutomationConstants.CONFIGURATION_DATA);
	}
	
	public String getGlobalVar(String key) {
		return data.getData(AutomationConstants.GLOBAL_DATA).getValue(key);
	}
	
	public String getScenarioVar(String scenario, String key) {
		return data.getData(AutomationConstants.SCENARIO_DATA).getValue(scenario, key);
	}
	
	public String getTestVar(String testId, String key) {
		return data.getData(AutomationConstants.TEST_DATA).getValue(testId, key);
	}
	
	public String getConfigVar(String key) {
		return data.getData(AutomationConstants.CONFIGURATION_DATA).getValue("config", key);
	}
	// endregion
	
	// region Setters	
	public void setTestData(DataObject testData) {
		this.data.replaceData(AutomationConstants.TEST_DATA, testData);
	}
	
	public synchronized void setReportPath(String path) {
		this.reportPath = path;
	}
	
	public synchronized void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public synchronized void setCaseVariables(String[] caseVariables) {
		this.caseVariables = caseVariables;
	}
	
	public void setGlobalVar(String key, String value) {
		data.getData(AutomationConstants.GLOBAL_DATA).setValue(key, value);
	}
	
	public void setScenarioVar(String scenario, String key, String value) {
		data.getData(AutomationConstants.SCENARIO_DATA).setValue(scenario, key, value);
	}
	
	public void setTestVar(String testId, String key, String value) {
		data.getData(AutomationConstants.TEST_DATA).setValue(testId, key, value);
	}
	
	public void setConfigVar(String key, String value) {
		data.getData(AutomationConstants.CONFIGURATION_DATA).setValue("config", key, value);
	}
	// endregion
	
	// region Data Access
	public String getVar(String rowKey, String key) {
		if(data.getData(AutomationConstants.GLOBAL_DATA).getValue(key) != null) {
			return data.getData(AutomationConstants.GLOBAL_DATA).getValue(key);
		}
		if(data.getData(AutomationConstants.SCENARIO_DATA).getRow(rowKey) != null
				&& data.getData(AutomationConstants.SCENARIO_DATA).getValue(rowKey, key) != null) {
			return data.getData(AutomationConstants.SCENARIO_DATA).getValue(rowKey, key);
		}
		if(data.getData(AutomationConstants.TEST_DATA).getRow(rowKey) != null
				&& data.getData(AutomationConstants.TEST_DATA).getValue(rowKey, key) != null) {
			return data.getData(AutomationConstants.TEST_DATA).getValue(rowKey, key);
		}
		
		return data.getValue(rowKey, key);
	}
	
	public void generateTestRow(String rowKey) {
		if(data.containsKey(AutomationConstants.TEST_DATA)) {
			data.getData(AutomationConstants.TEST_DATA).addRow(rowKey);
		} else {
			data.addData(AutomationConstants.TEST_DATA, new DataObject().addRow(rowKey));
		}
	}
	// endregion

	// region Data Setters
	public void addGlobalData(DataObject testData) {
		if(data.containsKey(AutomationConstants.GLOBAL_DATA)) {
			data.replaceData(AutomationConstants.GLOBAL_DATA, testData);
		} else {
			data.addData(AutomationConstants.GLOBAL_DATA, testData);
		}
	}
	
	public void addGlobalData(String testDataPath) {
		DataObject globalData = null;
		
		if(testDataPath != null) {
			try {
				globalData = new DataObject(FileUtils.csvFileToDMData(testDataPath));
				data.setKey(AutomationConstants.GLOBAL_DATA);
			} catch(Exception e) { 
				System.out.println("No global data file found");
				throw e;
			}
		}
		
		if(globalData != null && data.containsKey(AutomationConstants.GLOBAL_DATA)) {
			data.replaceData(AutomationConstants.GLOBAL_DATA, globalData);
		} else if(globalData != null) {
			data.addData(AutomationConstants.GLOBAL_DATA, globalData);
		}
	}
	
	public void addScenarioData(DataObject testData) {
		if(data.containsKey(AutomationConstants.SCENARIO_DATA)) {
			data.replaceData(AutomationConstants.SCENARIO_DATA, testData);
		} else {
			data.addData(AutomationConstants.SCENARIO_DATA, testData);
		}
	}
	
	public void addScenarioData(String scenarioDataFile) {
		DataObject scenarioData = null;
		
		if(scenarioDataFile != null) {
			try {
				scenarioData = new DataObject(FileUtils.csvFileToDMData(scenarioDataFile));
				data.setKey(AutomationConstants.SCENARIO_DATA);
			} catch(Exception e) { System.out.println("No scenario data file found");}
		}
		
		if(scenarioData != null && data.containsKey(AutomationConstants.SCENARIO_DATA)) {
			data.replaceData(AutomationConstants.SCENARIO_DATA, scenarioData);
		} else if(scenarioData != null) {
			data.addData(AutomationConstants.SCENARIO_DATA, scenarioData);
		}
	}
	
	public void addConfigurationData(String configDataFile) {
		DataObject conf = null;
		
		try {
			conf = new DataObject(FileUtils.variablesFileToArray(System.getProperty("user.dir") + "/" + AutomationConstants.RESOURCES_FOLDER + configDataFile));
		} catch(Exception e) { System.out.println("No configuration data file found");}
		
		if(conf != null && data.containsKey(AutomationConstants.CONFIGURATION_DATA)) {
			data.replaceData(AutomationConstants.CONFIGURATION_DATA, conf);
		} else if(conf != null) {
			data.addData(AutomationConstants.CONFIGURATION_DATA, conf);
		}
	}
	
	public void addTestData(String testDataFile) {
		DataObject testData = null;
		
		if(testDataFile != null) {
			try {
				testData = new DataObject(FileUtils.csvFileToMData(testDataFile));
				data.setKey(AutomationConstants.TEST_DATA);
			} catch(Exception e) { System.out.println("No scenario data file found");}
		}
		
		if(testData != null && data.containsKey(AutomationConstants.TEST_DATA)) {
			data.replaceData(AutomationConstants.TEST_DATA, testData);
		} else if(testData != null) {
			data.addData(AutomationConstants.TEST_DATA, testData);
		}
	}
	
	public void addTestData(DataObject testData) {
		if(data.containsKey(AutomationConstants.TEST_DATA)) {
			data.replaceData(AutomationConstants.TEST_DATA, testData);
		} else {
			data.addData(AutomationConstants.TEST_DATA, testData);
		}
	}
	
	public void addData(DataObject dataObject, String dataKey) {
		if(data.containsKey(dataKey)) {
			data.replaceData(dataKey, dataObject);
		} else {
			data.addData(dataKey, dataObject);
		}
	}
	
	public void addDMData(String fileName, String dataKey) {
		DataObject dataObject = new DataObject(FileUtils.csvFileToDMData(fileName));

		if(data.containsKey(dataKey)) {
			data.replaceData(dataKey, dataObject);
		} else {
			data.addData(dataKey, dataObject);
		}
	}
	
	public void addMData(String fileName, String dataKey) {
		DataObject dataObject = new DataObject(FileUtils.csvFileToMData(fileName));

		if(data.containsKey(dataKey)) {
			data.replaceData(dataKey, dataObject);
		} else {
			data.addData(dataKey, dataObject);
		}
	}
	// endregion
}
