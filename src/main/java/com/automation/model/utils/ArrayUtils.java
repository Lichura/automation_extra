package com.automation.model.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.automation.data.DataObject;

public class ArrayUtils {

	public static boolean stringInArray(String[] array, String string) {
		boolean result = false;
		
		for(int i = 0; i < array.length; i++) {
			if(array[i].equals(string)) {
				result = true;
				break;
			}
		}
		
		
		return result;
	}
	public static String[] concat(String[] firstArray, String[] secondArray) {
		String[] result = new String[firstArray.length + secondArray.length];
		
		for(int i = 0; i < firstArray.length; i++) {
			result[i] = firstArray[i];
		}
		
		for(int i = 0; i < secondArray.length; i++) {
			result[firstArray.length + i] = secondArray[i];
		}
		
		return result;
	}
	
	public static String[][] addIndexToMatrix(String[][] matrix) {
		String[][] result = new String[matrix.length][matrix[0].length + 1];

		for(int i = 0; i < matrix.length; i++) {
			result[i][0] = i + "";
			
			for(int j = 0; j < matrix[i].length; j++) result[i][j + 1] = matrix[i][j];
		}
		
		return result;
	}

	public static String[][] addColumnToMatrix(String[][] matrix, String string) {		
		return addColumnToMatrix(matrix, string, -1);
	}

	public static String[][] addColumnToMatrix(String[][] matrix, String string, int index) {
		index = index == -1 ? matrix[0].length : index;
		
		if(matrix.length > 0) {
			String[][] result = new String[matrix.length][matrix[0].length + 1];
	
			for(int i = 0; i < matrix.length; i++) {			
				for(int j = 0; j < matrix[i].length; j++) {
					result[i][j + (j >= index ? 1 : 0)] = matrix[i][j];
				}
				
				result[i][index] = string;
			}
			
			return result;
		} else {
			return new String[][]{{}};
		}
	}

	public static String[][] addColumnToMatrix(String[][] matrix, String[] array) {
		if(matrix.length == array.length) {
			String[][] result = new String[matrix.length][matrix[0].length + 1];
	
			for(int i = 0; i < matrix.length; i++) {
				for(int j = 0; j < matrix[i].length; j++) result[i][j] = matrix[i][j];
				
				result[i][matrix[0].length] = array[i];
			}
			
			return result;
		}
		
		return matrix;
	}

	public static String[][] addMatrixToMatrix(String[][] matrix, String[][] addedMatrix) {
		if(matrix == null) return addedMatrix;
		if(addedMatrix == null) return matrix;
		
		if(matrix.length > 0 && addedMatrix.length > 0 && matrix[0].length == addedMatrix[0].length) {
			String[][] result = new String[matrix.length + addedMatrix.length][matrix[0].length];
	
			for(int i = 0; i < matrix.length; i++) {
				result[i] = matrix[i];
			}
			
			for(int i = 0; i < addedMatrix.length; i++) {
				result[i + matrix.length] = addedMatrix[i];
			}
			
			return result;
		}
		
		return matrix;
	}

	public static String[][] removeRowFromMatrix(String[][] matrix, int row) {
		return removeRowFromMatrix(matrix, row, row);
	}

	public static String[][] removeRowFromMatrix(String[][] matrix, int initialRow, int finalRow) {
		if(initialRow <= finalRow && matrix.length > 0) {
			int line = 0;
			String[][] result = new String[matrix.length - (finalRow - initialRow + 1)][matrix[0].length];
			
			for(int i = 0; i < matrix.length; i++) {
				if(i < initialRow || i > finalRow) result[line++] = matrix[i];
			}
			
			return result;
		}
		return matrix;
	}

	public static String[][] removeColumnFromMatrix(String[][] matrix, int row) {
		return removeColumnFromMatrix(matrix, row, row);
	}

	public static String[][] removeColumnFromMatrix(String[][] matrix, int initialRow, int finalRow) {
		if(initialRow <= finalRow && matrix.length > 0) {
			String[][] result = new String[matrix.length][matrix[0].length - (finalRow - initialRow + 1)];
			
			for(int i = 0; i < matrix.length; i++) {
				int currentColumn = 0;
				
				for(int j = 0; j < matrix[0].length; j++) {
					if(j < initialRow || j > finalRow) {
						result[i][currentColumn] = matrix[i][j];
						currentColumn++;
					}
				}
			}
			
			return result;
		}
		
		return matrix;
	}

	public static String matrixToStringCSV(String[][] matrix) {
		String value = "";

		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				if(j != 0) value += ";";
				if(matrix[i][j] != null) value += matrix[i][j];
			}
			
			if(i + 1 < matrix.length) value += "\n";
		}

		return value;
	}

	public static String[] objetArrayToStringArray(Object[] array) {
		String[] result = new String[array.length];

		for(int i = 0; i < array.length; i++) {
			result[i] = array[i].toString();
		}

		return result;
	}

	public static HashMap<String, String> arrayToDMRow(String[] keyArray, String[] valueArray) {
		HashMap<String, String> result = new HashMap<String, String>();

		for(int i = 0; i < valueArray.length; i++) {
			result.put(keyArray[i], valueArray[i]);
		}

		return result;
	}

	public static DataObject matrixToDMData(String[][] matrix) {
		
		return new DataObject(FileUtils.csvStringToMData(ArrayUtils.matrixToString(matrix, "\n", ";")));
	}

	public static String[] subArray(String[] array, int initialPos, int finalPos) {
		int count = 0;
		String[] result = new String[finalPos - initialPos];

		for(int i = initialPos; i < finalPos; i++) {
			result[count++] = array[i];
		}

		return result;
	}

	public static String arrayToString(String[] array) {
		return arrayToString(array, " ");
	}

	public static String arrayToString(String[] array, String divider) {
		String result = "";

		for(int i = 0; i < array.length; i++) {
			if(i != 0) result += divider;
			result += array[i];
		}

		return result;
	}

	public static String matrixToString(String[][] matrix) {
		return matrixToString(matrix, "\n", "\t");
	}

	public static String matrixToString(String[][] matrix, String lineDiv, String columnDiv) {
		String result = "";

		for(int i = 0; i < matrix.length; i++) {
			if(i != 0) result += lineDiv;
			result += arrayToString(matrix[i], columnDiv);
		}

		return result;
	}
	
	public static boolean contains(String[] array, String string) {
		boolean result = false;
		
		for(String compareString : array) {
			if(compareString.equals(string)) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	public static String[][] removeRowsContaining(String[][] casesMatrix, String compareString, int index) {
		return removeRowsContaining(casesMatrix, new String[] {compareString}, index);
	}
	
	public static String[][] removeRowsContaining(String[][] casesMatrix, String[] containsArray, int index) {
		ArrayList<String[]> newArrays = new ArrayList<String[]>();
		
		for(int i = 0; i < casesMatrix.length; i++) {
			if(!contains(containsArray, casesMatrix[i][index])) {
				newArrays.add(casesMatrix[i]);
			}
		}
		
		String[][] result = new String[newArrays.size()][];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = newArrays.get(i);
		}
		
		return result;
	}
}
