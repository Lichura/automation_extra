package com.automation.model.utils;

import javax.mail.*;
import javax.mail.internet.*;

import com.automation.configuration.AutomationConstants;
import com.automation.model.utils.objects.HtmlElement;

import javax.activation.*;
import java.io.*;
import java.util.*;

/**
* Example how to construct a MHT (AKA MHTML) file in Java using JavaMail
* API. Creates a simple HTML page (from a String) with an image, which is
* pulled from a file. Requires mail.jar in the classpath (and also Java
* Activation Framework if JDK version is less than 1.6).
*
* The program expects a file named <em>pooh.jpg</em> in the current directory
* and produces a very simple MHT archive to stdout.
*/
public class HtmlToMht {
	
	public static void main(String[] args) {
		if(args.length >= 4 && !args[3].contains(",") && !args[3].contains(".")) {
			String reportPath = System.getProperty("user.dir") + "/" + AutomationConstants.REPORTS_FOLDER + "/T" + args[0] + args[1] + args[2];
			reportPath = reportPath.charAt(reportPath.length() - 1) == '/' ? reportPath : reportPath + "/";
			
			int relevantColumns = args.length > 5 ? Integer.parseInt(args[5]) : args.length > 4 && org.apache.commons.lang3.StringUtils.isNumeric(args[4]) ? Integer.parseInt(args[4]) : -1;
			
			String timeStamp = args[0] + "." + args[1] + "." + args[2] + "." + args[3] + 
				(args.length > 4  && !org.apache.commons.lang3.StringUtils.isNumeric(args[4]) ? "." + args[4] : "");
			
			
			HtmlElement htmlNode = CsvToHtml.createHtmlNode(timeStamp, reportPath, args[3], relevantColumns, null);
			
			try {
				generateMht(reportPath + timeStamp, htmlNode.toString().replace("thumbnails/", "").replace("images/", "")
					, ArrayUtils.concat(CsvToHtml.getThumbnailsPath(reportPath), CsvToHtml.getScreenshotsPath(reportPath)));
			} catch(Exception e) {}
		} else if (args.length >= 6 && (args[3].contains(",") || args[3].contains("."))){
			
			String year = args[0], month = args[1], day = args[2], browser = args[4];
			String reportPath = System.getProperty("user.dir") + "/" + AutomationConstants.REPORTS_FOLDER + "/T" + year + month + day;
			reportPath = reportPath.charAt(reportPath.length() - 1) == '/' ? reportPath : reportPath + "/";
			
			String[] testCases = args[3].contains(",") ? args[3].split(",") : args[3].split("\\.");
			
			
			String[] relevantColString;
			
			if(args.length > 6) {
				relevantColString = args[6].contains(",") ? args[6].split(",") : args[6].split("\\.");
			} else {
				relevantColString = new String[testCases.length];
			}
			
			int[] relevantColInt = new int[relevantColString.length];
			
			for(int i = 0; i < relevantColString.length; i++) {
				if(args.length == 7) {
					relevantColInt[i] = Integer.parseInt(relevantColString[i]);
				} else {
					relevantColInt[i] = -1;
				}
			}
			
			String timeStamp = year + "." + month + "." + day + ".[TESTCASE]." + browser;
			
			HtmlElement htmlNode = CsvToHtml.createJointHtmlNode(timeStamp, 
				reportPath, args[5], testCases, relevantColInt, null);

			try {
				generateMht(reportPath + timeStamp, htmlNode.toString().replace("thumbnails/", "").replace("images/", "")
					, ArrayUtils.concat(CsvToHtml.getThumbnailsPath(reportPath), CsvToHtml.getScreenshotsPath(reportPath)));
			} catch(Exception e) {}
		}
	}
	
	public static void generateMht(String filePath, String html, String[] imagesPath) throws Exception {
	    Properties props = new Properties();
	    Session session = Session.getInstance(props, null);

	    MimeMessage message = new MimeMessage(session);
	    MimeMultipart mpart = new MimeMultipart("related");

	    mpart.addBodyPart(bodyPart(
	        new StringSource("text/html", "index.html", html)));
	    
	    for(String path : imagesPath) {
	    	mpart.addBodyPart(bodyPart(new FileDataSource(path)));
	    }
	    
	    message.setContent(mpart);
	    message.setSubject("MHTML report");
	    message.addHeader("Content-Location", "index.html");
	
	    FileOutputStream out = new FileOutputStream(filePath + ".mhtml");
	    message.writeTo(out);
	    out.close();
	}
	
	private static BodyPart bodyPart(DataSource ds) throws MessagingException {
	    MimeBodyPart body = new MimeBodyPart();
	    DataHandler dh = new DataHandler(ds);
	    
	    body.setDisposition("inline");
	    body.setDataHandler(dh);
	    body.setFileName(dh.getName());
	    body.addHeader("Content-Location", dh.getName());
	    
	    return body;
	}
	
	private static final class StringSource implements DataSource {
		
	    private final String contentType;
	    private final String name;
	    private final byte[] data;
	    
	    public StringSource(String contentType, String name, String data) {
	        this.contentType = contentType;
	        this.data = data.getBytes();
	        this.name = name;
	    }
	    
	    public String getContentType() {
	        return contentType;
	    }
	    
	    public OutputStream getOutputStream() throws IOException {
	        throw new IOException();
	    }
	    
	    public InputStream getInputStream() throws IOException {
	        return new ByteArrayInputStream(data);
	    }
	    
	    public String getName() {
	        return name;
	    }
	}
}