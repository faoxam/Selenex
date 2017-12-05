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
public class Hover {
	
	static Logger logger = Logger.getLogger(Hover.class);

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
				expectedStr.append("Hower to (" + scenario.getScenarioTagged() + ") " + selString + "\n");
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
							"FAIL when hover Hower to (" 	+ scenario.getScenarioTagged() +") " 
							+ selString
							+ ". Reason:" + e.toString() + "\n");
				resultReport.setResult("FAIL");
				e.printStackTrace();
			}
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
