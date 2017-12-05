package my.com.selenex.util;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

/**
 * 
 * @author fa'izam
 *
 */
public class Helper {

	static Logger logger = Logger.getLogger(Helper.class);
	public Helper() {
		super();
	}

	private EventFiringWebDriver e_driver;
	private WebDriver driver;
	private WebDriverWait wait;
	private JSONObject actions;


	public EventFiringWebDriver getE_driver() {
		return e_driver;
	}

	public void setE_driver(EventFiringWebDriver e_driver) {
		this.e_driver = e_driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public void setWait(WebDriverWait wait) {
		this.wait = wait;
	}

	/**
	 * Constructor
	 * @param e_driver
	 * @param driver
	 * @param wait
	 */
	public Helper(EventFiringWebDriver e_driver, WebDriver driver, WebDriverWait wait, JSONObject actions) {
		this.e_driver = e_driver;
		this.driver = driver;
		this.wait = wait;
		this.actions = actions;
	}
	
	/**
	 * Constructor
	 * @param e_driver
	 * @param driver
	 * @param wait
	 */
	public Helper(EventFiringWebDriver e_driver, WebDriver driver, WebDriverWait wait) {
		this.e_driver = e_driver;
		this.driver = driver;
		this.wait = wait;
	}

	/**
	 * Constructor
	 * @param e_driver
	 * @param driver
	 */
	public Helper(EventFiringWebDriver e_driver, WebDriver driver) {
		this.e_driver = e_driver;
		this.driver = driver;
	}

	/**
	 * Constructor
	 * @param driver
	 */
	public Helper(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Hover to drop-down and click
	 * @param xPaths
	 */
	public boolean hoverAndClick(String[] xPaths, String errorReport) {
		logger.info("Hover And Click");
		try {
			Actions action = new Actions(driver);
			for (String xPath: xPaths) {
				WebElement we = e_driver.findElement(By.xpath(xPath));
				action.moveToElement(we);
			}
			action.click().build().perform();
		} catch (Exception e) {
			errorReport = e.toString();
			return false;
		}
		return true;
	}


	/**
	 * Enter input field
	 * @param xPath
	 * @param value
	 */
	public boolean setInputByXPath(String xPath, String value, String errorReport) {
		logger.info("Set Input By XPath");
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
			e_driver.findElement(By.xpath(xPath)).clear();
			e_driver.findElement(By.xpath(xPath)).sendKeys(value);
		} catch (Exception e) {
			errorReport = e.toString();
			return false;
		}
		return true;
	}

	/**
	 * Click to button/href
	 * @param xPath
	 */
	public boolean clickByXPath(String xPath, String errorReport) {
		logger.info("Click By XPath");
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
			e_driver.findElement(By.xpath(xPath)).click();
		} catch (Exception e) {
			errorReport = e.toString();
			return false;
		}
		return true;
	}


	/**
	 * 
	 * @param xPath
	 * @param expectedText
	 * @return
	 */
	public String validateText(String xPath, String expectedText, String errorReport) {
		logger.info("Validate Text");
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath)));
			WebElement webElement = driver.findElement(By.xpath(xPath));
			return webElement.getText();
		} catch (Exception e) {
			errorReport = e.toString();
			return null;
		}
	}

	/**
	 * navigate
	 * @param url
	 */
	public boolean navigate(String url, String errorReport) {
		logger.info("navigate");
		try {
			e_driver.navigate().to(url);
		} catch (Exception e) {
			errorReport = e.toString();
			return false;
		}
		return true;

	}

	/**
	 * getPageSource
	 * @return
	 */
	public String getPageSource() {
		return driver.getPageSource();
	}

	/**
	 * closeBrowser
	 */
	public void closeBrowser() {
		driver.quit();
	}

	/**
	 * dateDiffInMilis
	 * @param start
	 * @param end
	 * @return
	 */
	public String dateDiffInMilis(Date start, Date end) {
		long diff = end.getTime() - start.getTime();
		long milis = TimeUnit.MILLISECONDS.toMillis(diff);
		return String.valueOf(milis);
	}

	/**
	 * 
	 * @param aClass
	 * @param aMethod
	 * @param params
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private Object invoke (String aClass, String aMethod, Class<?>[] params, Object[] args)
			throws Exception {
		Class<?> c = Class.forName(aClass);
		Method m = c.getMethod(aMethod, params);
		Object i = c.newInstance();
		Object r = m.invoke(i, args);
		return r;
	}
	
	
	
	
	/**
	 * 
	 * @param scenario
	 * @param input
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	public ResultReport executeClass (
			Helper helper, 
			Scenario scenario, 
			LinkedHashMap<?, ?> input, 
			Map<?, ?> annotations, 
			int scenarioID, 
			int scenarioSeq) {

		ResultReport resultReport = new ResultReport();
		String methodName ="execute"; 
		String action = scenario.getAction().toLowerCase();
		Map<?, ?> actionMap = (Map<?, ?>) actions.get(action);
		String actionClassPackage = (String) actionMap.get("class");
		logger.info("Action:" + action);
		logger.info("Package:" + actionClassPackage);
		try {
			resultReport = (ResultReport) invoke(
				actionClassPackage, 
				methodName, 
				new Class[] {Helper.class, Scenario.class, LinkedHashMap.class, Map.class, int.class, int.class}, 
				new Object[] {helper, scenario, input, annotations, scenarioID, scenarioSeq}
			);
			return resultReport;
		} catch (Exception e) {
			e.printStackTrace();
		}


		return null;
	}

	
}


