package com.automation.model.utils;

import java.util.HashMap;

public class StringUtils {
	
	public static String correctUrlSlashes(String url) {
		String firstPart = "", secondPart = url;
		if(url.startsWith("http")) {
			firstPart = url.substring(0, url.indexOf("://") + 3);
			secondPart = url.substring(url.indexOf("://") + 3);
		}
		
		secondPart = secondPart.replaceAll("\\/+", "/");
		
		return firstPart + secondPart;
	}
	
	public static String snakeCaseToNatural(String snakeCaseText) {
		return snakeCaseText.substring(0, 1).toUpperCase() + snakeCaseText.substring(1).toLowerCase().replace("_", " ");
	}
	
	public static String camelCaseToNatural(String camelCaseText) {
		camelCaseText = camelCaseText.substring(0, 1).toUpperCase() + camelCaseText.substring(1);
		
		for(int i = 0; i < camelCaseText.length(); i++) {
			if(Character.isUpperCase(camelCaseText.charAt(i))) {
				camelCaseText = camelCaseText.substring(i) + " " + Character.toLowerCase(camelCaseText.charAt(i));
				i++;
			}
		}
		
		return camelCaseText;
	}
	
	public static String naturalToCamelCase(String naturalText) {
		naturalText = naturalText.toLowerCase();
		
		for(int i = 0; i < naturalText.length(); i++) {
			if(naturalText.charAt(i) == ' ') {
				naturalText = naturalText.substring(i) + naturalText.substring(i + 1);
				i--;
			}
		}
		
		return naturalText;
	}
	
	public static String naturalToSnakeCase(String naturalText) {
		return naturalText.substring(0, 1).toLowerCase() + naturalText.substring(1).toLowerCase().replace(" ", "_");
	}
	
	public static String replaceTextInBetween(String text, String replaceString, String leftSubstring, String rightSubstring) {
		String textInside = "";
		
		try {
			textInside = StringUtils.stringToArray(text, leftSubstring, rightSubstring)[0];
		} catch(ArrayIndexOutOfBoundsException e) {}
		
		return text.replace(leftSubstring + textInside + rightSubstring, leftSubstring + replaceString + rightSubstring);
	}

	public static HashMap<String, String> stringToDMRow(String text, String divider) {
		String key = null;
		HashMap<String, String> result = new HashMap<String, String>();

		for(int i = 0; i <= text.length(); i++) {
			if((i <= text.length() - divider.length() && text.substring(i, divider.length()).equals(divider)) || i == text.length()) {
				if(key == null) key = text.substring(0, i);

				result.put(key, text.substring(0, i));

				text = text.substring(i);
				if(text.startsWith(divider)) text = text.substring(divider.length());
				i = 0;
			}
		}

		return result;
	}

	public static HashMap<String, String> stringToDMRow(String[] keyArray, String[] valueArray) {
		HashMap<String, String> result = new HashMap<String, String>();

		for(int i = 0; i < valueArray.length; i++) {
			result.put(keyArray[i], valueArray[i]);
		}

		return result;
	}

	public static String mRowToString(HashMap<String, String> mappedRow, String divider) {
		String result = "", value = "";
		String[] keySet = ArrayUtils.objetArrayToStringArray(mappedRow.keySet().toArray());

		for(int i = 0; i < keySet.length; i++) {
			if(i != 0) result += divider;
			value = mappedRow.get(keySet[i]);
			result += value;
		}

		return result;
	}

	public static String dMRowToString(HashMap<String, String> mappedRow, String divider) {
		String result = "", value = "";
		String[] keySet = ArrayUtils.objetArrayToStringArray(mappedRow.keySet().toArray());

		for(int i = 0; i < keySet.length; i++) {
			if(i != 0) result += divider;
			value = mappedRow.get(keySet[i]);
			result += value;
		}

		return result;
	}

	public static String mDataToString(HashMap<String, HashMap<String, String>> mappedData) {
		return mDataToString(mappedData, ";");
	}

	public static String mDataToString(HashMap<String, HashMap<String, String>> mappedData, String divider) {
		String result = "";
		String[] keySet = ArrayUtils.objetArrayToStringArray(mappedData.keySet().toArray());
		HashMap<String, String> mappedRow = null;
		
		for(int i = 0; i < keySet.length; i++) {
			mappedRow = mappedData.get(keySet[i]);

			if(i == 0) result += ArrayUtils.arrayToString(ArrayUtils.objetArrayToStringArray(mappedRow.keySet().toArray()), divider);

			result += "\n";
			result += mRowToString(mappedRow, divider);
		}

		return result;
	}

	public static String dMDataToString(HashMap<String, HashMap<String, String>> mappedData) {
		return dMDataToString(mappedData, ";");
	}

	public static String dMDataToString(HashMap<String, HashMap<String, String>> mappedData, String divider) {
		String result = "";
		String[] keySet = ArrayUtils.objetArrayToStringArray(mappedData.keySet().toArray());
		HashMap<String, String> mappedRow = null;

		for(int i = 0; i < keySet.length; i++) {
			mappedRow = mappedData.get(keySet[i]);

			if(i == 0) result += ArrayUtils.arrayToString(ArrayUtils.objetArrayToStringArray(mappedRow.keySet().toArray()), divider);

			result += "\n";
			result += dMRowToString(mappedRow, divider);
		}

		return result;
	}

	public static int countOcurrencesInString(String text, String compareString) {
		int count = 0;

		for(int i = 0; i <= text.length() - compareString.length(); i++) {
			if(text.substring(i, i + compareString.length()).equals(compareString)) count++;
		}

		return count;
	}

	public static String[] stringToArray(String text, String divider) {
		int count = 0;
		String[] result = new String[countOcurrencesInString(text, divider) + 1];

		for(int i = 0; i <= text.length(); i++) {
			if(i + divider.length() <= text.length() && text.substring(i, i + divider.length()).equals(divider)) {
				result[count++] = text.substring(0, i);

				text = text.substring(i + 1);
				i = -1;
			} else if(i == text.length()) result[count] = text;
		}

		return result;
	}

	public static String[] stringToArray(String text, String leftDivider, String rightDivider) {
		boolean write = false;
		int count = 0, pos = 0, nested = 0;
		String[] result = new String[countOcurrencesInString(text, leftDivider)];

		for(int i = 0; i <= text.length(); i++) {
			if(write && i + leftDivider.length() <= text.length() && text.substring(i, i + leftDivider.length()).equals(leftDivider)) {
				nested++;
			} else if(write && i + rightDivider.length() <= text.length() && text.substring(i, i + rightDivider.length()).equals(rightDivider) && nested != 0) {
				nested--;
			} else if(!write && i + leftDivider.length() <= text.length() && text.substring(i, i + leftDivider.length()).equals(leftDivider)) {
				i = i + leftDivider.length() - 1;
				pos = i + 1;
				write = true;
			} else if(write && i + rightDivider.length() <= text.length() && text.substring(i, i + rightDivider.length()).equals(rightDivider) && nested == 0) {
				result[count++] = text.substring(pos, i);
				write = false;
			}
		}

		return result;
	}
}
