package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

/**
 * 
 * @author Fa'izam
 *
 */
public class SelectMultiple {
	
	static Logger logger = Logger.getLogger(SelectMultiple.class);
	
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
		resultReport.setExpected("To ensure ALL items (" + scenario.getValue().replaceAll("\n", ",")+ "), have been selected");

		Date start = new Date();
		resultReport.setStart(start);
		EventFiringWebDriver driver = helper.getE_driver();
		try {
			Select select = null;
			if (scenario.getSelectorType().equalsIgnoreCase("xPath")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(scenario.getSelectorString())));
				select = new Select(driver.findElement(By.xpath(scenario.getSelectorString())));
			}
			if (scenario.getSelectorType().equalsIgnoreCase("cssSelector")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(scenario.getSelectorString())));
				select = new Select(driver.findElement(By.cssSelector(scenario.getSelectorString())));
			}
			if (scenario.getSelectorType().equalsIgnoreCase("id")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id(scenario.getSelectorString())));
				select = new Select(driver.findElement(By.id(scenario.getSelectorString())));
			}
			if (scenario.getSelectorType().equalsIgnoreCase("className")) {
				helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className(scenario.getSelectorString())));
				select = new Select(driver.findElement(By.className(scenario.getSelectorString())));
			}
			if (!select.isMultiple()) {
				resultReport.setActual("Action '"+ scenario.getAction() + "' not applicable for single select. use action 'select-single' instead");
				resultReport.setResult("FAIL");
			}
			
			//By default it was the visible text value to select.
			//Valid annotation
			//@value. 		- By element ID
			//@text. 		- By element ID
			//@index.	- By selection index
			String[] selStrings = scenario.getSelectorString().split("\n");
			int index = -1;
			for (String sel: selStrings) {
				if (sel.startsWith("@value.")) {
					index = sel.indexOf("@value.".length() + 1);
					select.selectByValue(sel.substring(index));
				}
				else if (sel.startsWith("@text.")) {
					index = sel.indexOf("@text.".length() + 1);
					select.selectByVisibleText(sel.substring(index));
				}
				else if (sel.startsWith("@index.")) {
					index = sel.indexOf("@index.".length() + 1);
					select.selectByIndex(Integer.parseInt(sel.substring(index)));
				}
				else {
					select.selectByVisibleText(sel);
				}
			}
			
			resultReport.setActual("OK");
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
