package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

/**
 * 
 * @author Fa'izam
 *
 */
public class IsRadioNotSelected {

	static Logger logger = Logger.getLogger(IsRadioNotSelected.class);

	/**
	 * 
	 * @param helper
	 * @param scenario
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	private ResultReport execute(Helper helper, Scenario scenario, int scenarioID, int scenarioSeq) {

		ResultReport resultReport = new ResultReport();
		resultReport.setScenarioId("TS-" + scenarioID + "." + scenarioSeq);
		resultReport.setAction(scenario.getAction());
		resultReport.setSelectorString(scenario.getSelectorString());
		resultReport.setSelectorType(scenario.getSelectorType());
		resultReport.setDescription(scenario.getNote());
		resultReport.setExpected(
				"Ensure radio-item '" + scenario.getValue() + "' was NOT being selected");

		Date start = new Date();
		resultReport.setStart(start);
		EventFiringWebDriver driver = helper.getE_driver();

		try {

			WebElement radioGroup = null;

			if (scenario.getSelectorType().equalsIgnoreCase("xPath")) {
				helper.getWait()
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(scenario.getSelectorString())));
				radioGroup = driver.findElement(By.xpath(scenario.getSelectorString()));
			}
			if (scenario.getSelectorType().equalsIgnoreCase("cssSelector")) {
				helper.getWait().until(
						ExpectedConditions.visibilityOfElementLocated(By.cssSelector(scenario.getSelectorString())));
				radioGroup = driver.findElement(By.cssSelector(scenario.getSelectorString()));
			}
			if (scenario.getSelectorType().equalsIgnoreCase("id")) {
				helper.getWait()
						.until(ExpectedConditions.visibilityOfElementLocated(By.id(scenario.getSelectorString())));
				radioGroup = driver.findElement(By.id(scenario.getSelectorString()));
			}
			if (scenario.getSelectorType().equalsIgnoreCase("className")) {
				helper.getWait().until(
						ExpectedConditions.visibilityOfElementLocated(By.className(scenario.getSelectorString())));
				radioGroup = driver.findElement(By.className(scenario.getSelectorString()));
			}

			boolean isSelected = true;
			int index = -1;
			String sel = scenario.getValue();
			if (sel.startsWith("@id.")) {
				index = sel.indexOf("@id.".length() + 1);
				isSelected = radioGroup.findElement(By.id(scenario.getValue().substring(index))).isSelected();
			} 
			
			else if (sel.startsWith("@text.")) {
				index = sel.indexOf("@text.".length() + 1);
				isSelected = radioGroup.findElement(By.linkText(scenario.getValue().substring(index))).isSelected();
			} 
			
			else if (sel.startsWith("@name.")) {
				index = sel.indexOf("@name.".length() + 1);
				isSelected = radioGroup.findElement(By.name(scenario.getValue().substring(index))).isSelected();
			} 
			
			else if (sel.startsWith("@xpath.")) {
				index = sel.indexOf("@xpath.".length() + 1);
				isSelected = radioGroup.findElement(By.xpath(scenario.getValue().substring(index))).isSelected();
			} 
			
			else {
				isSelected = radioGroup.findElement(By.linkText(scenario.getValue())).isSelected();
			}

			if (!isSelected) {
				resultReport.setActual("Item " + scenario.getValue() + " was selected.");
				resultReport.setResult("PASS");
			}
			else {
				resultReport.setActual("Item " + scenario.getValue() + " was NOT selected.");
				resultReport.setResult("FAIL");
			}
			
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
	public ResultReport execute(Helper helper, Scenario scenario, LinkedHashMap<?, ?> input, Map<?, ?> annotations,
			int scenarioID, int scenarioSeq) {

		return execute(helper, scenario, scenarioID, scenarioSeq);
	}

}
