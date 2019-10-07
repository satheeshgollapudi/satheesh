package com.seleniumFramework.helper.mouseover;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.seleniumFramework.helper.DropDown.DropDownHelper;
import com.seleniumFramework.helper.Logger.LoggerHelper;

public class Mouseover {
	

	private WebDriver driver;
	private Logger Log = LoggerHelper.getLogger(Mouseover.class);

	public Mouseover(WebDriver driver) {
		this.driver = driver;
		Log.debug("DropDownHelper : " + this.driver.hashCode());
	}

	public void mouseOver(String data){
		//log.info("doing mouse over on :"+data);
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath("//*[contains(text(),'"+data+"')]"))).build().perform();
	}
	
	public void mouseOver1(WebElement element){
		//log.info("doing mouse over on :"+data);
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
	}
}
