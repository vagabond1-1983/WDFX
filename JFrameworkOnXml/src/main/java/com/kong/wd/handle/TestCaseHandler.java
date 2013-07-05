package com.kong.wd.handle;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import com.kong.wd.model.IBean;
import com.kong.wd.model.TestItem;

public class TestCaseHandler extends Handler {
	private WebDriver driver;
	
	
	public TestCaseHandler(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	public WebDriver handle(IBean bean) {
		if(bean == null) {
			throw new NullPointerException("test case bean does not exist");
		}
		TestItem testBean = (TestItem)bean;
		if(driver == null) {
			throw new NullPointerException("driver is null while under test case handling");
		}
		driver.manage().timeouts().implicitlyWait(testBean.getTimeout(), TimeUnit.SECONDS);
		return driver;
	}


}
