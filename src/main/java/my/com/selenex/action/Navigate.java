package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

/**
 * 
 * @author Fa'izam
 *
 */
public class Navigate {

	static Logger logger = Logger.getLogger(Navigate.class);
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
		String url = scenario.getSelectorString();
		resultReport.setExpected("Navigate to  (" + scenario.getSelectorType() + ") :" + scenario.getSelectorString());
		try {
			helper.getE_driver().navigate().to(url);
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
