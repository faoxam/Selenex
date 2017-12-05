package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

public class IsElementExist {
	
	static Logger logger = Logger.getLogger(IsElementExist.class);

	/**
	 * 
	 * @param helper
	 * @param scenario
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	private ResultReport execute (
			Helper helper,
			Scenario scenario,
			int scenarioID, 
			int scenarioSeq) {
		ResultReport resultReport = new ResultReport();
		resultReport.setScenarioId("TS-"+ scenarioID + "." + scenarioSeq);
		resultReport.setAction(scenario.getAction());
		resultReport.setSelectorString(scenario.getSelectorString());
		resultReport.setSelectorType(scenario.getSelectorType());
		resultReport.setDescription(scenario.getNote());
		
		
		Date start = new Date();
		resultReport.setStart(start);
		resultReport.setExpected("Validate isExist  (" + scenario.getSelectorType() + ") :" + scenario.getSelectorString());
		try {
			WebElement webElement = null;
			if (scenario.getSelectorType().equalsIgnoreCase("xpath")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(scenario.getSelectorString())));
				webElement = helper.getDriver().findElement(By.xpath(scenario.getSelectorString()));
			}
			else if (scenario.getSelectorType().equalsIgnoreCase("cssSelector")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(scenario.getSelectorString())));
				webElement = helper.getDriver().findElement(By.cssSelector(scenario.getSelectorString()));
			}
			else if (scenario.getSelectorType().equalsIgnoreCase("id")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id(scenario.getSelectorString())));
				webElement = helper.getDriver().findElement(By.id(scenario.getSelectorString()));
			}
			else if (scenario.getSelectorType().equalsIgnoreCase("className")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className(scenario.getSelectorString())));
				webElement = helper.getDriver().findElement(By.className(scenario.getSelectorString()));
			}
			resultReport.setActual(webElement.getText());
			resultReport.setResult("PASS");
			
		} catch (Exception e) {
			resultReport.setActual(e.toString());
			resultReport.setResult("FAIL");
			e.printStackTrace();
		}
		Date end = new Date();
		resultReport.setEnd(end);
		resultReport.setConsumingTime(helper.dateDiffInMilis(start, end));
		return resultReport;
	}


	/**
	 * 
	 * @param helper
	 * @param scenario
	 * @param annotations
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	public ResultReport execute (
			Helper helper,
			Scenario scenario, 
			LinkedHashMap<?, ?> input, 
			Map<?, ?> annotations, 
			int scenarioID, 
			int scenarioSeq) {
		
		return execute (
				helper,
				scenario, 
				scenarioID, 
				scenarioSeq);
	}
	
}
