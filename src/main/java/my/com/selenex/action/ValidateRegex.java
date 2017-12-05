package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

public class ValidateRegex {

	static Logger logger = Logger.getLogger(ValidateRegex.class);
	
	/**
	 * 
	 * @param scenario
	 * @param input
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	private ResultReport execute (
			Helper helper,
			Scenario scenario, 
			LinkedHashMap<?, ?> input, 
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
		resultReport.setExpected(scenario.getValue());
		try {
			WebElement webElement = null;
			if (scenario.getSelectorType().equalsIgnoreCase("xpath")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(scenario.getSelectorString())));
				webElement = helper.getE_driver().findElement(By.xpath(scenario.getSelectorString()));
			}
			else if (scenario.getSelectorType().equalsIgnoreCase("cssSelector")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(scenario.getSelectorString())));
				webElement = helper.getE_driver().findElement(By.cssSelector(scenario.getSelectorString()));
			}
			else if (scenario.getSelectorType().equalsIgnoreCase("id")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id(scenario.getSelectorString())));
				webElement = helper.getE_driver().findElement(By.id(scenario.getSelectorString()));
			}
			else if (scenario.getSelectorType().equalsIgnoreCase("className")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className(scenario.getSelectorString())));
				webElement = helper.getE_driver().findElement(By.className(scenario.getSelectorString()));
			}
			
			resultReport.setActual(webElement.getText());
			Pattern pattern = Pattern.compile(scenario.getValue().replaceAll(" ","").replaceAll("\n", "").replaceAll("\r", ""));
			Matcher matcher = pattern.matcher(webElement.getText().replaceAll(" ","").replaceAll("\n", "").replaceAll("\r", ""));
			resultReport.setResult(matcher.matches() == true ? "PASS" : "FAIL");
			
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
	 * @param input
	 * @param colValidationTable
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	public ResultReport execute (
			Helper helper,
			Scenario scenario, 
			LinkedHashMap<?, ?> input, 
			Map<?, ?> annotation, 
			int scenarioID, 
			int scenarioSeq) {

		return execute (
				helper,
				scenario, 
				input, 
				scenarioID, 
				scenarioSeq);
	}
}
