package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;
import my.com.selenex.vo.SelectList;

/**
 * 
 * @author Fa'izam
 *
 */
public class SelectSingle {
	
	static Logger logger = Logger.getLogger(SelectSingle.class);
	
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
		resultReport.setExpected("Select single item from elements at (" + scenario.getSelectorType()+ "), " + scenario.getSelectorString());

		Date start = new Date();
		resultReport.setStart(start);
		try {
			Select select = helper.selectElement(scenario);
			
			if (select.isMultiple()) {
				resultReport.setActual("Action "+ scenario.getAction() + " not applicable for multiple select. use action 'select-multiple'");
				resultReport.setResult("FAIL");
			}
			else {
				String sel = scenario.getValue();
				int index = -1;
				if (sel.startsWith(SelectList.value_annotation)) {
					index = sel.indexOf(SelectList.value_annotation.length() + 1);
					select.selectByValue(sel.substring(index));
				}
				else if (sel.startsWith(SelectList.text_annotation)) {
					index = sel.indexOf(SelectList.text_annotation.length() + 1);
					select.selectByVisibleText(sel.substring(index));
				}
				else if (sel.startsWith(SelectList.index_annotation)) {
					index = sel.indexOf(SelectList.index_annotation.length() + 1);
					select.selectByIndex(Integer.parseInt(sel.substring(index)));
				}
				else {
					select.selectByVisibleText(sel);
				}
							
				resultReport.setActual("OK");
				resultReport.setResult("PASS");
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
