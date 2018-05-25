package com.automation.model.helpers;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SizeFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileHelper {
	public static void setClipboardData(String filePath) {
		StringSelection stringSelection = new StringSelection(filePath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

	public static void uploadFIle(String fileNameAndPath) throws AWTException {
		setClipboardData(fileNameAndPath);
		// native key strokes for CTRL, V and ENTER keys

		Robot robot = new Robot();
		robot.delay(2000);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}

	public boolean CheckIfFilesInFolderHaveBeenDownloaded(File tempFolder, int timeout) {
		Collection<File> finishedFiles = null;
		LocalDateTime currentDateTimeDownload = LocalDateTime.now();

		do {
			try {
				Thread.sleep(500L);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}

			finishedFiles = FileUtils.listFiles(tempFolder, new SizeFileFilter(1, true), TrueFileFilter.TRUE);

		} while(finishedFiles.isEmpty() && currentDateTimeDownload.plusSeconds(timeout).isAfter(LocalDateTime.now())
			&& finishedFiles.size() == 0);
		if(FileUtils.listFiles(tempFolder, new String[]{ "crdownload"}, false).isEmpty()) { return true; }
		return false;
	}
}
