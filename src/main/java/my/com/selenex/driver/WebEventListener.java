package my.com.selenex.driver;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import my.com.selenex.action.ValidateRegexTableColumns;

public class WebEventListener extends AbstractWebDriverEventListener {
	
	static Logger logger = Logger.getLogger(WebEventListener.class);
	public WebEventListener() {
		super();
	}
	
	
	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		logger.info("Before navigating to: '" + url + "'");
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		logger.info("Navigated to:'" + url + "'");
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		logger.info("Trying to click on: " + element.toString());
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		logger.info("Clicked on: " + element.toString());
	}

	@Override
	public void onException(Throwable error, WebDriver driver) {
		logger.info("Error occurred: " + error);
	}
}
