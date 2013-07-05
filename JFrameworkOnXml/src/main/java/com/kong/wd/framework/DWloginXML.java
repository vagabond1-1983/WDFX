package com.kong.wd.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

/**
 * @author Zhu Shang Yuan
 * 
 */
public class DWloginXML {
	private static WebDriver driver;
	private static List<Element> stepNodes;
	private static int timeout = 90;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// settings
		String application_url = "http://www.google.com.hk/";
		String browser = "*iexplore";
		String seleniumServer = "127.0.0.1";
		String seleniumPort = "4444";
		String ucXMLFile = System.getProperty("user.dir") + File.separator
				+ "UseCases" + File.separator + "GoogleDWTest.xml";
		;
		Document document = null;
		try {
			System.out.println("��������XML�ļ�λ��::" + ucXMLFile);
			document = new SAXReader().read(new File(ucXMLFile));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element suiteElm = document.getRootElement();
		// settings element
		Element settings = suiteElm.element("SETTINGS");
		if (settings != null) {
			browser = settings.attributeValue("browser");
			seleniumServer = settings.attributeValue("seleniumServer");
			seleniumPort = settings.attributeValue("seleniumPort");
			application_url = settings.attributeValue("application_url");
		}

		// TEST element
		Element testElm = suiteElm.element("TEST");
		System.out.println("������������?:" + testElm.attributeValue("name"));
		if (testElm.attributeValue("timeout") != null) {
			// ������timeout����,������Ĭ�����ã�
			timeout = new Integer(testElm.attributeValue("timeout")).intValue();
			System.out.println("��ʱ���ã�timeout="
					+ testElm.attributeValue("timeout") + "��");
		}

		stepNodes = testElm.element("STEPS").elements();
		try {
			if (browser.contains("ie")) {

				// Set webdriver.ie.driver and copy IEDriverServer.exe to any
				// location.
				// Else you will get java.lang.IllegalStateException: The path
				// to the driver executable must be set by the
				// webdriver.ie.driver system property
				System.setProperty("webdriver.ie.driver",
						"driver/IEDriverServer.exe");
				DesiredCapabilities ieCapabilities = DesiredCapabilities
						.internetExplorer();
				ieCapabilities
						.setCapability(
								InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
								true);
				driver = new InternetExplorerDriver(ieCapabilities);
				// new Integer(seleniumPort).intValue());
			} else if (browser.contains("firefox")) {
				System.setProperty("webdriver.firefox.profile",
						"driver/about config.xul");
				FirefoxProfile profile = new FirefoxProfile();
				driver = new FirefoxDriver(profile);
			}
			Thread.sleep(5000);
			System.out.println("��������Selenium������");
			driver.get(application_url);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		Element elm = null;
		// ѭ���������Բ���
		Iterator<Element> iterator = null;
		iterator = stepNodes.iterator();
		while (iterator != null && iterator.hasNext()) {// whileѭ����ʼ
			elm = iterator.next();
			System.out.println("------------------------------>");
			System.out.println("step index=" + elm.attributeValue("index"));
			System.out.println("step name=" + elm.attributeValue("name"));
			System.out.println("step type=" + elm.attributeValue("type"));
			String type = elm.attributeValue("type");
			if (type == null) {
				SeleneseTestBase.fail("���붨��type���ԣ�����XML��������");
			}
			if (type.equals("Type.CLICK")) {
				// ����Click��������
				driver.manage().timeouts()
						.implicitlyWait(timeout, TimeUnit.SECONDS);
				try {
					String xpValue = elm.element("XPATH").getText();
					driver.findElement(By.xpath(xpValue)).click();

				} catch (Exception e) {
					SeleneseTestBase.fail(e.getMessage()
							+ "\n����ִ��ʧ�ܣ�����ִ�б���ֹ,����Ԫ��Ϊ:\n" + elm.asXML());
				}
			} else if (type.equals("Type.INPUT")) {
				// ����Input �������ִ���������
				try {
					driver.manage().timeouts()
							.implicitlyWait(timeout, TimeUnit.SECONDS);
					String inValue = elm.element("VALUE").getText();
					driver.findElement(By.xpath(elm.element("XPATH").getText()))
							.sendKeys(inValue);
				} catch (Exception e) {
					SeleneseTestBase.fail(e.getMessage()
							+ "\n����ִ��ʧ�ܣ�����ִ�б���ֹ,����Ԫ��Ϊ:\n" + elm.asXML());
				}
			} else if (type.equals("Type.CaptureScreenshot")) {
				driver.manage().timeouts()
						.implicitlyWait(timeout, TimeUnit.SECONDS);
				String fileName = elm.element("VALUE").getText();
				captureScreenshot(fileName);
			}
		}// ѭ���������Բ��裻whileѭ������
	}

	/**
	 * fileName �����ͼ���ļ���?
	 */
	private void captureScreenshot(String fileName) {

		String imagePath = System.getProperty("user.dir") + File.separator
				+ "screenshot" + File.separator + fileName + ".png";
		try {
			String base64Screenshot = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.BASE64);
			byte[] decodedScreenshot = Base64.decodeBase64(base64Screenshot
					.getBytes());
			FileOutputStream fos = new FileOutputStream(new File(imagePath));
			fos.write(decodedScreenshot);
			fos.close();
			System.out.println("��ͼ������" + imagePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
