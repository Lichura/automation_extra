package com.automation.data;

import java.util.HashMap;

import com.automation.model.utils.ArrayUtils;

/**
 * The DataObject class is used to manage HashMaps<String, HashMap<String,
 * String> objects, this objects usually come from a .csv file and are mapped by
 * column and row, if there is not a unique key for the rows a key is created
 * using the index of the row.
 * 
 * This object is useful to access to .csv data with few parameters,
 * at much 2 parameters are used (column and row), but there is a key String
 * used to set the default row, so only the column key is needed.
 *
 * @author Alfredo Moises Boullosa Ramones
 */
public class DataObject {

	private String key;
	HashMap<String, HashMap<String, String>> data;

	// region Constructors
	public DataObject() {
		this.data = new HashMap<String, HashMap<String, String>>();
	}

	public DataObject(HashMap<String, HashMap<String, String>> data) {
		this.data = data;
		if(data.keySet().size() > 0) setKey(ArrayUtils.objetArrayToStringArray(data.keySet().toArray())[0]);
	}
	// endregion

	// region Getters
	public int size() {
		return data.size();
	}

	public boolean containsKey(String key) {
		return this.data.containsKey(key);
	}

	public boolean containsValue(String value) {
		return this.data.containsValue(value);
	}

	public String getKey() {
		return key;
	}

	public String[] getKeySet() {
		return ArrayUtils.objetArrayToStringArray(data.keySet().toArray());
	}

	public HashMap<String, String> getRow() {
		return getRow(key);
	}

	public HashMap<String, HashMap<String, String>> getMap() {
		return data;
	}

	public HashMap<String, String> getRow(String valueKey) {
		return data.get(valueKey);
	}

	public String getValue(String valueKey) {
		return getValue(key, valueKey);
	}

	public String getValue(String rowKey, String valueKey) {
		try {
			if(rowKey != null) return getRow(rowKey).get(valueKey);
		} catch(NullPointerException e) {
			System.out.println("NullPointerException: There is no row with value \"" + rowKey + "\"");
		}

		return null;
	}

	public String toString() {
		String res = "";

		if(getKeySet().length > 0) {
			res += ArrayUtils.arrayToString(getKeySet(), "\t") + "\n";
			String[] rowKeySet = ArrayUtils.objetArrayToStringArray(getRow(getKeySet()[0]).keySet().toArray());

			for(int i = 0; i < getKeySet().length; i++) {
				if(i != 0) {
					res += "\n";
				}

				for(int j = 0; j < rowKeySet.length; j++) {
					if(j != 0) {
						res += "\t";
					}

					res += data.get(getKeySet()[i]).get(rowKeySet[j]);
				}
			}
		}

		return res;
	}
	// endregion

	// region Setters
	public DataObject addRow(String rowKey) {
		if(!this.data.containsKey(rowKey)) {
			synchronized(data) {
				this.data.put(rowKey, new HashMap<String, String>());
			}
		}

		return this;
	}

	public DataObject setKey(String key) {
		if(!data.containsKey(key)) System.out.println("NullPointerException: There is no row with value \"" + key + "\"");
		this.key = key;

		return this;
	}

	public void setValue(String valueKey, String value) {
		setValue(key, valueKey, value);
	}

	public void setValue(String rowKey, String valueKey, String value) {
		try {
			synchronized(data) {
				if(rowKey != null && getRow(rowKey).containsKey(valueKey)) {
					getRow(rowKey).replace(valueKey, value);
				} else if(rowKey != null) getRow(rowKey).put(valueKey, value);
			}
		} catch(NullPointerException e) {
			System.out.println("NullPointerException: There is no row with value \"" + rowKey + "\"");
		}
	}

	public void duplicateDataByN(int n) {
		for(int i = 1; i < n; i++) {
			for(int j = 0; j < size(); j++) {
				duplicateRow(Integer.toString(((size() * i) + j)), Integer.toString(j));
			}
		}
	}

	public void duplicateRow(String newRow, String rowKey) {
		HashMap<String, String> auxiliarHash = new HashMap<String, String>();
		String[] keys = new String[0];
		
		try {
			keys = ArrayUtils.objetArrayToStringArray(data.get(rowKey).keySet().toArray());
		} catch(NullPointerException e) {
			System.out.println("NullPointerException: There is no row with value \"" + rowKey + "\"");
		}

		for(int i = 0; i < keys.length; i++) {
			auxiliarHash.put(keys[i], data.get(rowKey).get(keys[i]));
		}

		synchronized(data) {
			data.put(newRow, auxiliarHash);
		}
	}
	// endregion
}