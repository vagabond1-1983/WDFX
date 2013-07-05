package com.kong.wd.framework;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kong.wd.common.Util;
import com.kong.wd.dao.impl.XmlParse;
import com.kong.wd.handle.Handler;
import com.kong.wd.handle.InitEnvHandler;
import com.thoughtworks.selenium.SeleneseTestBase;

/**
 * @author Zhu Shang Yuan
 * 
 */
public class RunMain {
	private static WebDriver driver;
	private static List<Element> stepNodes;
	private static int timeout = 90;
	private static final Logger logger = Logger.getLogger(RunMain.class);
	private static XmlParse xmlParseDao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File ucXMLFile = new File(System.getProperty("user.dir")
				+ File.separator + "UseCases" + File.separator
				+ "BaiduDWTest.xml");
		xmlParseDao = new XmlParse(ucXMLFile);
		Element settingElm = xmlParseDao.findElementUnderRoot("SETTINGS");
		LaunchAppParamsBean settings = xmlParseDao.parseSettings(settingElm);
		Handler handler = new InitEnvHandler();
		driver = handler.initEnv(settings);
		
		logger.debug("Selenium server started!");
		driver.get(settings.getApplication_url());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		/*
		 * if (driver != null) { System.out.println("ֹͣSelenium��");
		 * driver.close(); driver.quit();
		 * 
		 * }
		 */
	}

	@Test
	public void test() {
		TestBean testBean = xmlParseDao.parseTest(xmlParseDao
				.findElementUnderRoot("TEST"));
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		WebElement testElement = null;
		Iterator<StepBean> stepIterator = testBean.getSteps().iterator();
		while (stepIterator.hasNext()) {
			StepBean step = stepIterator.next();
			String type = step.getType();
			String xpath = step.getXpath();
			String value = step.getValue();
			try {
				if (null != xpath && !xpath.isEmpty()) {
					testElement = driver.findElement(By.xpath(xpath));
				}
			} catch (Exception e) {
				SeleneseTestBase.fail(e.getMessage()
						+ "\nElement does not find:\n" + step.toString());
			}
			if (type.equals("Type.CLICK")) {
				// Test Element click
				testElement.click();
			} else if (type.equals("Type.INPUT")) {
				// Test Element input
				testElement.sendKeys(value);
			} else if (type.equals("Type.CaptureScreenshot")) {
				String fileName = value;
				Util.captureScreenshot(driver, fileName);
			}
		}
	}

}