package com.mytests;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.sauce.Common_Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SauceTest {	
	
	WebDriver driver = null;
	Common_Utilities Testdata = new Common_Utilities();
	
	@Parameters({"browser"})
	@BeforeTest
	public void setup(String browserName, Method name) throws IOException {
	
		System.out.println("browser name is:"+browserName);
			
		String remote = Testdata.getpropertyvalue("remote", "Test_config.properties");
		
		if (remote.matches("true")) {

			//AWS: EC2 Linux Instance Using Docker Grid: HubUrl / EC2 Linux Remote Url
			String remoteUrl = "http://3.16.155.42:4444/wd/hub";
			System.out.println("Linux hub url is:"+remoteUrl);
				
			//AWS: EC2 Linux: Capabilities
			DesiredCapabilities cap = new DesiredCapabilities();
					
			if (browserName.equalsIgnoreCase("chrome")) {
				WebDriverManager.chromedriver().setup();
				cap.setCapability("browserName", "chrome");			
			}
			else if(browserName.equalsIgnoreCase("firefox")) {
				WebDriverManager.firefoxdriver().setup();
				cap.setCapability("browserName", "firefox");
			}
			else if(browserName.equalsIgnoreCase("ie")) {
				WebDriverManager.iedriver().setup();
				cap.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
			}
			else if(browserName.equalsIgnoreCase("Edge")) {
				WebDriverManager.edgedriver().setup();
				cap.setCapability(CapabilityType.BROWSER_NAME, BrowserType.EDGE);
			}
			//Selenium Grid: will run on perticular Cloud(AWS, Azure, GCP)
			try {			
				driver = new RemoteWebDriver(new URL(remoteUrl),cap);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
		
	    } 
		else if (remote.matches("false")) {
	    	
			if (browserName.equalsIgnoreCase("chrome")) {
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
			}
			else if(browserName.equalsIgnoreCase("firefox")) {
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
			}
			else if(browserName.equalsIgnoreCase("ie")) {
				WebDriverManager.iedriver().setup();
				driver = new InternetExplorerDriver();
			}
			else if(browserName.equalsIgnoreCase("Edge")) {
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
			}
	    }
	    	
	}
	
	@Test(priority = 1)
	public void LoginTest() throws IOException {
		
		String URL = Testdata.getpropertyvalue("URL", "Test_config.properties");
		String UserName = Testdata.getpropertyvalue("UserName", "Test_config.properties");
		String Password = Testdata.getpropertyvalue("Password", "Test_config.properties");
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(URL);
		driver.manage().window().maximize();
		
		System.out.println(driver.getTitle());
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("user-name")).sendKeys(UserName);
		driver.findElement(By.id("password")).sendKeys(Password);
		driver.findElement(By.id("login-button")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	@Test(priority = 2)
	public void HomeTest() {
		
		//checkInventoryItemTest:
		Assert.assertTrue(driver.findElements(By.xpath("//div[@class='inventory_item']")).size() == 6);
		
		//checkAddToCartButtonTest:
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to cart']")).size() == 6);
		
		//appLogoTest:
		Assert.assertTrue(driver.findElement(By.xpath("//div[@class='app_logo']")).isDisplayed());
		
		//socialTwitterTest:
		Assert.assertTrue(driver.findElement(By.className("social_twitter")).isDisplayed());
		
		//socialFacebookTest:
		Assert.assertTrue(driver.findElement(By.className("social_facebook")).isDisplayed());
		
		//socialLinkedInTest:
		Assert.assertTrue(driver.findElement(By.className("social_linkedin")).isDisplayed());		
	}
	
	@Test(priority = 3)
	public void sauceLinksTest() {
		List<WebElement> sauceLinksList = driver.findElements(By.cssSelector("a"));
		sauceLinksList.forEach(ele -> System.out.println(ele.getText()));
		Assert.assertEquals(sauceLinksList.size(), 20);
	}
	
	@Test(priority = 4)
	public void Lougout() throws InterruptedException {
		
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		driver.findElement(By.id("react-burger-menu-btn")).click();
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(By.id("logout_sidebar_link")).click();
	}
	
	@AfterTest
	public void teardown() throws InterruptedException {		
		Thread.sleep(2000);
		driver.quit();
		Thread.sleep(2000);
	}
}
