package com.automation.data;

import java.util.HashMap;

import com.automation.model.utils.ArrayUtils;
import com.automation.model.utils.FileUtils;

/**
 * The DataManagerObject class is used to manage DataObject objects, it can hold
 * several DataObjects and gives access to their variables through this class.
 *
 * @author Alfredo Moises Boullosa Ramones
 */
public class DataManagerObject {

	private int size = 0;
	private String key;
	HashMap<String, DataObject> mappedData = new HashMap<String, DataObject>();

	public DataManagerObject() {}

	public DataManagerObject(String key, DataObject data) {
		this.key = key;
		addData(key, data);
	}

	public DataManagerObject(String key, HashMap<String, HashMap<String, String>> data) {
		this.key = key;
		addData(key, new DataObject(data));
	}

	// region Getters
	public DataObject getData(String dataKey) {
		return this.mappedData.get(dataKey);
	}

	public String[] getKeySet() {
		return ArrayUtils.objetArrayToStringArray(this.mappedData.keySet().toArray());
	}

	public int size() {
		return this.size;
	}

	public boolean containsKey(String dataKey) {
		return this.mappedData.containsKey(dataKey);
	}

	public boolean containsValue(String dataValue) {
		return this.mappedData.containsKey(dataValue);
	}
	// endregion

	// region Setters
	public void setKey(String key) {
		this.key = key;
	}

	public void addData(String key, DataObject dataObject) {
		synchronized(mappedData) {
			size++;
			this.mappedData.put(key, dataObject);
		}
	}

	public void removeData(String key) {
		synchronized(mappedData) {
			if(this.mappedData.containsKey(key)) size--;
			this.mappedData.remove(key);
		}
	}

	public void replaceData(String dataKey, DataObject dataObject) {
		synchronized(mappedData) {
			this.removeData(dataKey);
			this.addData(dataKey, dataObject);
		}
	}

	public void addMDataFromFile(String key, String fileName) {
		synchronized(mappedData) {
			this.addData(key, new DataObject(FileUtils.csvFileToMData(fileName)));
		}
	}

	public void addDMDataFromFile(String key, String fileName) {
		synchronized(mappedData) {
			this.addData(key, new DataObject(FileUtils.csvFileToDMData(fileName)));
		}
	}

	public String getValue(String key) {
		String[] mappedKeys = getKeySet();

		for(int i = 0; i < this.mappedData.size(); i++) {
			String[] setKeys = ArrayUtils.objetArrayToStringArray(this.mappedData.get(mappedKeys[i]).getRow().keySet().toArray());

			for(String setKey : setKeys) {
				if(setKey.equals(key)) {
					return this.mappedData.get(mappedKeys[i]).getRow().get(key);
				}
			}
		}

		return null;
	}

	public String getValue(String row, String key) {
		String[] keySet = getKeySet();

		for(String currentData : keySet) {
			if(this.mappedData.get(currentData).getRow(row) != null
					&& this.mappedData.get(currentData).getRow(row).get(key) != null) {
				return this.mappedData.get(currentData).getValue(row, key);
			}
		}

		return null;
	}

	public void setValue(String key, String value) {
		boolean found = false;
		String dataKey = null;
		String[] mappedKeys = getKeySet();

		for(int i = 0; i < mappedKeys.length && !found; i++) {
			String[] rowKeys = this.mappedData.get(mappedKeys[i]).getKeySet();
			dataKey = mappedKeys[i];

			for(String row : rowKeys) {
				if(row.equals(key)) {
					this.mappedData.get(row).setValue(key, value);
					found = true;
					break;
				}
			}
		}

		if(!found) this.mappedData.get(this.key == null ? dataKey : this.key).setValue(key, value);
	}

	public void setValue(String row, String key, String value) {
		boolean found = false;
		String dataKey = null;
		String[] mappedKeys = getKeySet();

		for(int i = 0; i < mappedKeys.length && !found; i++) {
			if(this.mappedData.get(mappedKeys[i]).containsKey(row)) {
				dataKey = mappedKeys[i];

				if(this.mappedData.get(mappedKeys[i]).getRow(row).containsKey(key)) {
					this.mappedData.get(mappedKeys[i]).setValue(row, key, value);
					found = true;
					break;
				}
			}
		}

		if(!found && dataKey != null) this.mappedData.get(dataKey).setValue(row, key, value);
	}
	// endregion
}