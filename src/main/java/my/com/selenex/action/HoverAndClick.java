package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

/**
 * 
 * @author Fa'izam
 *
 */
public class HoverAndClick  {
	
	static Logger logger = Logger.getLogger(HoverAndClick.class);
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
		String[] selStrings = scenario.getSelectorString().split("\n");
		Actions action = new Actions(helper.getDriver());

		StringBuffer expectedStr = new StringBuffer();
		StringBuffer actualStr = new StringBuffer();
		for (String selString: selStrings) {

			try {
				expectedStr.append("Hower to "+ selString + ".\n");
				if (scenario.getSelectorType().equalsIgnoreCase("xPath")) {
					WebElement we = helper.getE_driver().findElement(By.xpath(selString));
					action.moveToElement(we);
				}
				if (scenario.getSelectorType().equalsIgnoreCase("cssSelector")) {
					WebElement we = helper.getE_driver().findElement(By.cssSelector(selString));
					action.moveToElement(we);
				}
				if (scenario.getSelectorType().equalsIgnoreCase("id")) {
					WebElement we = helper.getE_driver().findElement(By.id(selString));
					action.moveToElement(we);
				}
				if (scenario.getSelectorType().equalsIgnoreCase("className")) {
					WebElement we = helper.getE_driver().findElement(By.className(selString));
					action.moveToElement(we);
				}
				actualStr.append("Successful Hover to  (" 	+ scenario.getScenarioTagged() +"):	"+ selString + "\n");
			} catch (Exception e) {
				actualStr.append(	
							"FAIL when hover Hower to (" 	+ scenario.getScenarioTagged() +"): " 
							+ selString 
							+ ". Reason:" + e.toString() + "\n");
				resultReport.setResult("FAIL");
			}
			 
		}
		
		int lastHoverIdx = selStrings.length - 1;
		try {
			action.click().build().perform();
			actualStr.append("Successful click to  (" 	+ scenario.getScenarioTagged() +"): "+ selStrings[lastHoverIdx] + "\n");
		} catch (Exception e) {
			
			actualStr.append(	
					"FAIL when click to (" 	+ scenario.getScenarioTagged() +") " 
					+ selStrings[lastHoverIdx]
					+ ". Reason:" + e.toString() + "\n");
			resultReport.setResult("FAIL");
			e.printStackTrace();
		}
		
		resultReport.setExpected(expectedStr.toString());

		Date end = new Date();
		resultReport.setEnd(end);
		resultReport.setConsumingTime(helper.dateDiffInMilis(start, end));
		if (!resultReport.getResult().equals("FAIL")) {
			resultReport.setActual("OK");
			resultReport.setResult("PASS");
		}

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
