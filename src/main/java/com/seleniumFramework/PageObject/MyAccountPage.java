package com.seleniumFramework.PageObject;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.seleniumFramework.helper.Logger.LoggerHelper;
import com.seleniumFramework.helper.Wait.WaitHelper;
import com.seleniumFramework.helper.assertionHelper.VerificationHelper;
import com.seleniumFramework.testBase.Config;
import com.seleniumFramework.testBase.TestBase;



public class MyAccountPage {

	WebDriver driver;
	private final Logger log = LoggerHelper.getLogger(MyAccountPage.class);
	WaitHelper waitHelper;
	
	@FindBy(xpath="//*[contains(text(),'Welcome to your account. Here you can manage all of your personal information and orders.')]")
	WebElement successRegistrationMsg;
	
	@FindBy(xpath="//*[contains(text(),'Order history and details')]")
	WebElement OrderHistoryAndDetails;
	
	@FindBy(xpath="//*[contains(text(),'My personal information')]")
	WebElement MyPersonalInformation;
	
	public MyAccountPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitHelper = new WaitHelper(driver);
		TestBase testBase = new TestBase();
		waitHelper.waitForElement(driver, OrderHistoryAndDetails,new Config(testBase.OR).getExplicitWait());
	}
	
	public boolean verifySuccessRegistrationMsg(){
		return VerificationHelper.verifyElementPresent(successRegistrationMsg);
	}
}
