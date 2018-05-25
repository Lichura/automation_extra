package com.automation.model.utils;

import java.util.ArrayList;

public class DriverUtils {

	public static String cleanAppiumPage(String page, String appPackage) {
		page = page.replaceAll(">", ">\n");
		page = page.replaceAll("resource-id=\"\" ", "").replaceAll(appPackage + ":id/", "");
		page = page.replaceAll("package=\"" + appPackage + "\" ", "");
		page = page.replaceAll("content-desc=\"\" ", "");
		page = page.replaceAll("checkable=\"false\" checked=\"false\" ", "");
		page = page.replaceAll("password=\"false\" selected=\"false\" ", "");
		page = page.replaceAll("long-clickable=\"false\" ", "");
		page = page.replaceAll("clickable=\"false\" ", "");
		page = page.replaceAll("enabled=\"true\" ", "");
		page = page.replaceAll("focusable=\"false\" ", "");
		page = page.replaceAll("focused=\"false\" ", "");
		page = page.replaceAll("scrollable=\"false\" ", "");
		page = page.replaceAll("focusable=\"true\" ", "");
		page = page.replaceAll("scrollable=\"true\" ", "");
		page = page.replaceAll("text=\"\" ", "");

		return page;
	}

	public static String[] getAppiumClickableElements(String page) {
		String[] html = StringUtils.stringToArray(page, "\n");
		ArrayList<String> elements = new ArrayList<String>();

		for(int i = 0; i < html.length; i++) {
			if(html[i].contains("<android") && ((!html[i].contains("LinearLayout") && !html[i].contains("RelativeLayout")
				&& !html[i].contains("view.View") && !html[i].contains("FrameLayout") && !html[i].contains("RecyclerView")
				&& !html[i].contains("DrawerLayout")) || html[i].contains("resource-id="))) {
				//if(html[i].contains("resource-id=")) System.out.println("LINE: " + html[i]);
				elements.add(html[i]);
			}
		}

		return ArrayUtils.objetArrayToStringArray(elements.toArray());
	}
}
