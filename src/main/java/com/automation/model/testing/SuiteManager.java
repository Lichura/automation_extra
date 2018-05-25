package com.automation.model.testing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.automation.configuration.AutomationConstants;
import com.automation.data.DataObject;
import com.automation.model.utils.ArrayUtils;
import com.automation.model.utils.FileUtils;
import com.automation.model.utils.InitUtils;
import javafx.util.Pair;

/**
 * The SuiteManager class is used to manage test objects 
 * like the TestDataManager and the result matrix.
 *
 * @author Alfredo Moises Boullosa Ramones
 */
public class SuiteManager {

	private String suiteName;
	private ArrayList<Pair<String, Integer>> testCases = new ArrayList<Pair<String, Integer>>();
	private HashMap<String, Pair<TestDataManager, String[][]>> testSuiteObject = new HashMap<String, Pair<TestDataManager, String[][]>>();
	
	public SuiteManager(String suiteName) {
		this.suiteName = suiteName;
	}
	
	public String getName() {
		return suiteName;
	}
	
	public synchronized TestDataManager getTestData(String testCase) {
		TestDataManager testDataM = null;
		
		if(testSuiteObject.get(testCase) != null) {
			testDataM = testSuiteObject.get(testCase).getKey();
		}
		
		return testDataM;
	}
	
	public String[][] getResultMatrix(String testCase) {
		String[][] testDataM = null;
		
		if(testSuiteObject.get(testCase) != null) {
			testDataM = testSuiteObject.get(testCase).getValue();
		}
		
		return testDataM;
	}
	
	public synchronized String[] getTestCases() {
		String[] result = new String[testCases.size()];

		for(int i = 0; i < result.length; i++) {
			result[i] = testCases.get(i).getKey();
		}
		
		return result;
	}
	
	public synchronized int[] getRelevantColumns() {
		int[] result = new int[testCases.size()];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = testCases.get(0).getValue();
		}
		
		return result;
	}
	
	public synchronized void setRelevantColumn(String testCase, int relevantColumn) {
		for(int i = 0; i < testCases.size(); i++) {
			if(testCases.get(i).getKey().equals(testCase)) {
				testCases.set(i, new Pair<String, Integer>(testCase, relevantColumn));
			}
		}
	}
	
	public synchronized String[][] removeMobileCases(String testCase, String[][] casesMatrix) {
		String[][] resultMatrix = getResultMatrix(testCase);
		String[][] result = ArrayUtils.removeRowsContaining(casesMatrix, InitUtils.getMobileBrowsers(), 2);
		
		resultMatrix = ArrayUtils.removeRowsContaining(resultMatrix, InitUtils.getMobileBrowsers(), 2);
		
		if(casesMatrix.length == 0) {
			testCases.remove(testCase);
			testSuiteObject.remove(testCase);
		} else {
			testSuiteObject.replace(testCase, new Pair<TestDataManager, String[][]>(testSuiteObject.get(testCase).getKey(), resultMatrix));
		}
		
		return result;
	}
	
	public synchronized void addTestObjects(String testCase, TestDataManager testDataM, String[][] resultMatrix) {
		testSuiteObject.put(testCase, new Pair<TestDataManager, String[][]>(testDataM, resultMatrix));
		testCases.add(new Pair<String, Integer>(testCase, -1));
	}

	public String[][] initializeTestObjects(String testCase) {
		return initializeTestObjects(testCase, null, null);
	}

	public String[][] initializeTestObjects(String testCase, String scenarioDataPath) {
		return initializeTestObjects(testCase, scenarioDataPath, null);
	}

	public String[][] initializeTestObjects(String testCase, String scenarioDataPath, String testDataPath) {
		System.out.println("[INFO] - Case: " + testCase);
		String[][] casesMatrix = null;
		String defaultTestData = InitUtils.getTestDataPath(testDataPath), defaultScenarioData = scenarioDataPath;
		String[] browsers = InitUtils.getTestBrowsers();

		String[][] resultMatrix = null;
		TestDataManager testData = InitUtils.initializeTestData(defaultTestData, defaultScenarioData, AutomationConstants.CONFIGURATION_DATA_SET);
		
		testData.generateTimeStamp(testCase);

		if(defaultTestData != null) {	
			testData.addTestData(defaultTestData);
			testData.setCaseVariables(FileUtils.loadDataFileToArray(defaultTestData, true)[0]);
		} else {
			HashMap<String, String> auxRow =  new HashMap<String, String>();
			HashMap<String, HashMap<String, String>> hashMapAux = new HashMap<String, HashMap<String, String>>();
			
			auxRow.put("id", "0");
			hashMapAux.put("0", auxRow);
			
			DataObject testDataDefault = new DataObject(hashMapAux);

			testData.addTestData(testDataDefault);
			testData.setCaseVariables(new String[]{"id"});
		}

		testData.getTestData().duplicateDataByN(browsers.length);

		if(testData.getDailyCase().contains("continue") && new File(testData.getReportPath() + testData.getTimeStamp() + ".csv").exists()) {
			System.out.println("[INFO] - Continue daily");
			resultMatrix = InitUtils.getResultMatrixFromCsvFile(testData.getReportPath() + testData.getTimeStamp() + ".csv");
			casesMatrix = InitUtils.getCasesMatrixFromResultMatrix(resultMatrix, testCase);

		} else {
			casesMatrix = InitUtils.getCasesMatrixFromBrowserArray(testCase, browsers, testData.getTestData().size());
			resultMatrix = InitUtils.getResultMatrixFromTestData(testData.getTestData(), browsers, testData.getCaseVariables());
		}

		addTestObjects(testCase, testData, resultMatrix);
		
		System.out.println("[INFO] - Test to run on this execution: " + casesMatrix.length);

		return casesMatrix.length == 0 ? null : casesMatrix;
	}
}
