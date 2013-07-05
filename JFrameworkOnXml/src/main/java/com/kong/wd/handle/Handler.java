package com.kong.wd.handle;

import org.openqa.selenium.WebDriver;

import com.kong.wd.model.LaunchAppParamsBean;

public abstract class Handler {
	private WebDriver driver;

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public abstract WebDriver initEnv(LaunchAppParamsBean settings);
	

}
