package com.seleniumFramework.testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.seleniumFramework.excelReader.ExcelReader;
import com.seleniumFramework.helper.Wait.WaitHelper;



public class TestBase {
	
	public static final Logger logger = Logger.getLogger(TestBase.class.getName());
	public WebDriver driver;
	public static Properties OR;
	public File f1;
	public FileInputStream file;
	 public ExcelReader excelreader;
	
	   public static ExtentReports extent;
		public static ExtentTest test;
	
		public ITestResult result;
	
		static {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			extent = new ExtentReports(System.getProperty("user.dir") + "/src/main/java/com/hybridFramework/report/test" + formater.format(calendar.getTime()) + ".html", false);
		}
		
		@BeforeTest
		public void launchBrowser(){
			try {
				loadPropertiesFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Config config = new Config(OR);
			getBrowser(config.getBrowser());
			WaitHelper waitHelper = new WaitHelper(driver);
			waitHelper.setImplicitWait(config.getImplicitWait(), TimeUnit.SECONDS);
			waitHelper.setPageLoadTimeout(config.getPageLoadTimeOut(), TimeUnit.SECONDS);
		}
		
	public void getBrowser(String browser){
		if(System.getProperty("os.name").contains("Window")){
			if(browser.equalsIgnoreCase("firefox")){
				//https://github.com/mozilla/geckodriver/releases
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/drivers/geckodriver.exe");
				driver = new FirefoxDriver();
			}
			else if(browser.equalsIgnoreCase("chrome")){
				System.out.println(System.getProperty("user.dir"));
				//https://chromedriver.storage.googleapis.com/index.html
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/chromedriver.exe");
				driver = new ChromeDriver();
			}
		}
		else if(System.getProperty("os.name").contains("Mac")){
			System.out.println(System.getProperty("os.name"));
			if(browser.equalsIgnoreCase("firefox")){
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/drivers/geckodriver");
				driver = new FirefoxDriver();
			}
			else if(browser.equalsIgnoreCase("chrome")){
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/chromedriver");
				driver = new ChromeDriver();
			}
		}
	}
	
	public void loadPropertiesFile() throws IOException{
		
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		OR = new Properties();
		f1 = new File(System.getProperty("user.dir")+"/src/main/java/com/seleniumFramework/config/config.properties");
		file = new FileInputStream(f1);
		OR.load(file);
		logger.info("loading config.properties");
		
		f1 = new File(System.getProperty("user.dir")+"/src/main/java/com/seleniumFramework/config/or.properties");
		file = new FileInputStream(f1);
		OR.load(file);
		logger.info("loading or.properties");
	
	}
	
	public String getScreenShot(String imageName) throws IOException{
		
		if(imageName.equals("")){
			imageName = "blank";
		}
		File image = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String imagelocation = System.getProperty("user.dir")+"/src/main/java/com/hybridFramework/screenshot/";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		String actualImageName = imagelocation+imageName+"_"+formater.format(calendar.getTime())+".png";
		File destFile = new File(actualImageName);
		FileUtils.copyFile(image, destFile);
		return actualImageName;
	}
	
	public WebElement waitForElement(WebDriver driver,long time,WebElement element){
		WebDriverWait wait = new WebDriverWait(driver, time);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public WebElement waitForElementWithPollingInterval(WebDriver driver,long time,WebElement element){
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.pollingEvery(5, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public void impliciteWait(long time){
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}
	
	public void getresult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.SUCCESS) {

			test.log(LogStatus.PASS, result.getName() + " test is pass");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
		} else if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.FAIL, result.getName() + " test is failed" + result.getThrowable());
			String screen = getScreenShot("");
			test.log(LogStatus.FAIL, test.addScreenCapture(screen));
		} else if (result.getStatus() == ITestResult.STARTED) {
			test.log(LogStatus.INFO, result.getName() + " test is started");
		}
	}

	@AfterMethod()
	public void afterMethod(ITestResult result) throws IOException {
		getresult(result);
	}

	@BeforeMethod()
	public void beforeMethod(Method result) {
		test = extent.startTest(result.getName());
		test.log(LogStatus.INFO, result.getName() + " test Started");
	}
	
	@AfterClass(alwaysRun = true)
	public void endTest() {
		//driver.quit();
		extent.endTest(test);
		extent.flush();
	}
	
	public String[][] getData(String excelName, String sheetName){
		System.out.println(System.getProperty("user.dir"));
		String excellocation = System.getProperty("user.dir")+"/src/main/java/com/seleniumFramework/data/"+excelName;
		System.out.println(excellocation);
		excelreader = new ExcelReader();
		return excelreader.getExcelData(excellocation, sheetName);
	}
	
public static void main(String[] args) throws IOException {
	TestBase test = new TestBase();
	test.loadPropertiesFile();
	//test.getBrowser("chrome");
	System.out.println(test.OR.getProperty("Username"));
}
}
