package com.automation.model.utils;

import com.automation.model.utils.objects.HtmlElement;

public class HtmlUtils {
	
	public static HtmlElement newCeld() {
		return new HtmlElement("th");
	}
	
	public static HtmlElement newRow() {
		return new HtmlElement("tr");
	}
	
	public static HtmlElement newTable() {
		return new HtmlElement("tr");
	}
	
	public static HtmlElement createRow(int columns) {
		HtmlElement rowNode = new HtmlElement("tr");
		
		for(int i = 0; i < columns; i++) {
			rowNode.addChild("th", "", "");
		}
		
		return rowNode;
	}
	
	public static HtmlElement createTable(int rows, int columns) {
		HtmlElement tableNode = new HtmlElement("tbody");
		
		for(int i = 0; i < rows; i++) {
			tableNode.addChild(createRow(columns));
		}
		
		return new HtmlElement("table").addChild(tableNode);
	}
	
	public static HtmlElement createRow(String[] array) {
		HtmlElement rowNode = new HtmlElement("tr");
		
		for(int i = 0; i < array.length; i++) {
			rowNode.addChild("th", "", array[i]);
		}
		
		return rowNode;
	}
	
	public static HtmlElement createRow(HtmlElement[] array) {
		HtmlElement rowNode = new HtmlElement("tr");
		
		for(int i = 0; i < array.length; i++) {
			rowNode.addChild(
				new HtmlElement("th", "", "")
					.addChild(array[i]));
		}
		
		return rowNode;
	}
	
	public static HtmlElement createTable(String[][] matrix) {
		HtmlElement tableNode = new HtmlElement("tbody");
		
		for(int i = 0; i < matrix.length; i++) {
			tableNode.addChild(
				new HtmlElement("tr", "", "")
					.addChild(createRow(matrix[i])));
		}
		
		return new HtmlElement("table").addChild(tableNode);
	}
	
	public static HtmlElement createTable(HtmlElement[][] matrix) {
		HtmlElement tableNode = new HtmlElement("table");
		
		for(int i = 0; i < matrix.length; i++) {
			tableNode.addChild(
				new HtmlElement("tr", "", "")
					.addChild(createRow(matrix[i])));
		}
		
		return tableNode;
	}
}
