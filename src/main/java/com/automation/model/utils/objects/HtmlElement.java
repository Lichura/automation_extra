package com.automation.model.utils.objects;

import java.util.ArrayList;

import com.automation.model.utils.ArrayUtils;

/**
 * The HtmlElement class is a node structure containing elements that a HTML element contains,
 * like tag, attributes and content, this class is used to construct an HTML page by nodes
 * and then it is printed in a recursive way to generate the HTML using the method toString()
 *
 * @author Alfredo Moises Boullosa Ramones
 */
public class HtmlElement {
	
	private String tag;
	private String id = "";
	private String attributes = "";
	private String content = "";
	private HtmlElement parentNode = null;
	private ArrayList<HtmlElement> childs = new ArrayList<HtmlElement>();
	private static String[] notFinishingTags = new String[]{"br", "hr"};
	
	public HtmlElement(String tag) {
		this.tag = tag;
	}
	
	public HtmlElement(String tag, String attributes) {
		this.tag = tag;
		this.attributes = attributes;
	}
	
	public HtmlElement(String tag, String attributes, String content) {
		this.tag = tag;
		this.attributes = attributes;
		this.content = content;
	}
	
	public HtmlElement(String tag, HtmlElement parentNode) {
		this.tag = tag;
		this.parentNode = parentNode;
	}
	
	public HtmlElement(String tag, String attributes, HtmlElement parentNode) {
		this.tag = tag;
		this.attributes = attributes;
		this.parentNode = parentNode;
	}
	
	public HtmlElement(String tag, String attributes, String content, HtmlElement parentNode) {
		this.tag = tag;
		this.attributes = attributes;
		this.content = content;
		this.parentNode = parentNode;
	}
	
	// region Html element methods
	public String getTag() {
		return tag;
	}
	
	public String getAttributes() {
		return attributes;
	}
	
	public String getContent() {
		return content;
	}
	
	public HtmlElement setTag(String tag) {
		this.tag = tag;
		
		return this;
	}
	
	public HtmlElement setAttributes(String attributes) {
		this.attributes = attributes;
		
		return this;
	}
	
	public HtmlElement addAttribute(String attribute, String value) {
		this.attributes += " " + attribute + "=\"" + value + "\"";
		
		return this;
	}
	
	public HtmlElement addStyle(String value) {
		String style = "style=\"";
		if(attributes.contains(style)) {
			for(int i = 0; i < attributes.length(); i++) {
				if(i - style.length() >= 0 && attributes.substring(i - style.length(), i).equals(style)) {
					String styleContent = attributes.substring(i);
					styleContent = styleContent.substring(0, styleContent.indexOf('"'));
					
					attributes = attributes.replace(styleContent, styleContent + " " + value);
				}
			}
		} else {
			addAttribute("style", value);
		}
		
		return this;
	}
	
	public HtmlElement setContent(String content) {
		this.content = content;
		
		return this;
	}
	// endregion
	
	// region Node methods
	public String getId() {		
		return id;
	}
	
	public HtmlElement getParent() {		
		return parentNode;
	}
	
	public int getLevel() {		
		return this.parentNode == null ? 0 : this.parentNode.getLevel() + 1;
	}
	
	public ArrayList<HtmlElement> getChilds() {		
		return childs;
	}
	
	public HtmlElement setId(String newId) {
		this.id = newId;
		
		return this;
	}
	
	public HtmlElement setParent(HtmlElement parent) {
		this.parentNode = parent;
		
		return this;
	}
	
	public HtmlElement getChild(int index) {
		return childs.get(index);
	}
	
	public HtmlElement getChildByTag(String tag) {		
		return getChildByTag(tag, 0);
	}
	
	public HtmlElement getChildByTag(String tag, int nth) {
		int currentIndex = 0;
		HtmlElement child = null;
		
		for(int i = 0; i < childs.size(); i++) {
			if(childs.get(i).getTag().equals(tag)) {
				if(currentIndex == nth) {
					child = childs.get(i);
				}
				
				currentIndex++;
			}
		}
		
		return child;
	}
	
	public HtmlElement addChild(String tag) {
		return addChild(tag, "", "");
	}
	
	public HtmlElement addChild(String tag, String attribute) {
		return addChild(tag, attribute, "");
	}
	
	public HtmlElement addChild(String tag, String attribute, String content) {
		HtmlElement element = new HtmlElement(tag, attribute, content);
		childs.add(element);
		element.setParent(this);
		
		return this;
	}
	
	public HtmlElement addChild(HtmlElement child) {
		childs.add(child);
		child.setParent(this);
		
		return this;
	}
	
	public HtmlElement addChildAt(HtmlElement child, int index) {
		childs.add(index, child);
		child.setParent(this);
		
		return this;
	}
	
	public HtmlElement removeChild(HtmlElement child) {
		childs.remove(child);
		
		return this;
	}
	
	
	public HtmlElement removeChildAt(int index) {
		childs.remove(index);
		
		return this;
	}
	
	public HtmlElement cloneNode() {
		HtmlElement clonedNode = new HtmlElement(tag, attributes, content);
		
		for(int i = 0; i < this.childs.size(); i++) {
			clonedNode.addChild(childs.get(i).cloneNode());
		}
		
		return clonedNode;
	}
	
	public String toString() {
		String htmlText = "", tab = "";
		
		for(int i = 0; i < getLevel(); i++) {
			tab += "\t";
		}
		
		if(getTag().isEmpty()) {
			htmlText += getContent();
		} else {
			htmlText += tab + "<" + getTag()
				+ (getAttributes().isEmpty() ? "" : " " + getAttributes()) + ">"
				+ (getContent().isEmpty() ? "" : "\n\t" + tab + getContent());
		}
		
		for(int i = 0; i < getChilds().size(); i++) {
			htmlText += "\n" + getChilds().get(i).toString();
		}
		
		if(!getTag().isEmpty() && !ArrayUtils.stringInArray(notFinishingTags, getTag())) {
			htmlText += "\n" + tab + "</" + getTag() + ">";
		}
		
		return htmlText;
	}
	// endregion
}
