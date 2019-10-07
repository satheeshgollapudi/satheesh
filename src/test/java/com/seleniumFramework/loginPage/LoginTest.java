package com.seleniumFramework.loginPage;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.seleniumFramework.PageObject.LoginPage;
import com.seleniumFramework.helper.Logger.LoggerHelper;
import com.seleniumFramework.testBase.Config;
import com.seleniumFramework.testBase.TestBase;



public class LoginTest extends TestBase {

	private final Logger log = LoggerHelper.getLogger(LoginTest.class);
	
	@Test
	public void testLoginToApplication(){
		log.info(LoginTest.class.getName()+" started");
		Config config = new Config(OR);
		driver.get(config.getWebsite());
		
		LoginPage loginPage = new LoginPage(driver);
		
		loginPage.loginToApplication(config.getUserName(), config.getPassword());
		boolean status = loginPage.verifySuccessLoginMsg();
		if(status){
		   log.info("login is sucessful");	
		}
		else{
			Assert.assertTrue(false, "login is not sucessful");
		}
	}

}
