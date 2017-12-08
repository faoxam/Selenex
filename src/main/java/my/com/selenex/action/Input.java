package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

/**
 * 
 * @author Fa'izam
 *
 */
public class Input {

	static Logger logger = Logger.getLogger(Input.class);
	
	/**
	 * 
	 * @param scenario
	 * @param input
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	private ResultReport automateInput (
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
		
		int idx = scenario.getValue().indexOf(".");
		String fieldName = scenario.getValue().substring(idx + 1);
		String inputValue = (String) input.get(fieldName);
		if (null == inputValue) {
			inputValue = scenario.getValue();
		}
		resultReport.setExpected("Enter  " + inputValue + " into " + scenario.getSelectorString());
		
		try {
			helper.performInput(scenario, inputValue);
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
			Map<?,?> annotation, 
			int scenarioID, 
			int scenarioSeq) {
		
		return automateInput (
				helper,
				scenario, 
				input, 
				scenarioID, 
				scenarioSeq);
	}
}
