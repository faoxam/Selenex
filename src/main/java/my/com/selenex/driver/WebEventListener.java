package my.com.selenex.driver;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class WebEventListener extends AbstractWebDriverEventListener {
	
	protected Logger logger;
	public WebEventListener(Logger logger) {
		super();
		this.logger = logger;
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
