package my.com.selenex.action;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.Cascade;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;

public class ValidateCascadeList {

	static Logger logger = Logger.getLogger(ValidateCascadeList.class);
	
	/**
	 * 
	 * @param scenario
	 * @param input
	 * @param scenarioID
	 * @param scenarioSeq
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ResultReport execute (
			Helper helper,
			Scenario scenario, 
			LinkedHashMap<?, ?> validateElement, 
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
			int idx = scenario.getValue().indexOf(".");
			String valSheetName = scenario.getValue().substring(idx + 1);
			List<Cascade> cascades = (List<Cascade>) validateElement.get(valSheetName);

			StringBuffer actualResult = new StringBuffer();
			String result = "PASS";
			for (Cascade cascade: cascades) {
				int i = 0;
				while (true) {
					i ++;
					String element = scenario.getSelectorString()+ "/" + cascade.getParent() + "["+ i +"]" + cascade.getChild();
					logger.info("Elements:" + element);
					if (i == 1) {
						helper.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
					}
					WebElement child;
					try {
						child = helper.getE_driver().findElement(By.xpath(element));
						if (!child.isDisplayed() || !child.isEnabled()) {
							actualResult.append("Box " + i + " does not appear. xpath= "+ scenario.getSelectorString() + "/" + child.getTagName() +"["+ i +"]"  + child.getText() + "\n");
							result = "FAIL";
							break;
						}
					} catch (Exception e) {
						if (i == 1) {
							actualResult.append("Error. " + e.toString());
							result = "FAIL";
						}
						break;
					}

					String text = child.getText();
					String regex = cascade.getRegex();
					Pattern pattern = Pattern.compile(regex.replaceAll(" ",""));
					Matcher matcher = pattern.matcher(text.replaceAll(" ",""));
					if (matcher.matches()) {
						actualResult.append("MATCHES. Box =" + i + ", " +  cascade.getRemark() + "\n");
					}
					else {
						actualResult.append("NOT MATCHES. Element at box " + i + ", " + cascade.getRemark() + "'" + child.getText() + 
								"'  vs regex '" + regex + "'. xpath= " 
								+ scenario.getSelectorString() 
								+ "/" + child.getTagName() +"["+ i +"]" 
								+ child.getText() + "\n");
						result = "FAIL";
					}	

				}
			}
			resultReport.setActual(actualResult.substring(0, actualResult.length() -1));
			resultReport.setResult(result);

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

		LinkedHashMap<?, ?> cascade = new LinkedHashMap<>();
		cascade = (LinkedHashMap<?, ?>) annotation.get(Cascade.annotation);
		return execute (
				helper,
				scenario, 
				cascade, 
				scenarioID, 
				scenarioSeq);
	}
}
