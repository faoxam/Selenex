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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import my.com.selenex.util.Helper;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;
import my.com.selenex.vo.Texts;

public class ValidateRegexs {

	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	static Logger logger = Logger.getLogger(ValidateRegexs.class);
	
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
			LinkedHashMap<?, ?> texts, 
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
			WebElement webElement = helper.findElement(scenario);
			
			int idx = scenario.getValue().indexOf(Texts.indicator);
			String valSheetName = scenario.getValue().substring(idx + Texts.indicator.length());
			logger.info("valSheetName:"+ valSheetName);
			logger.info("texts:"+ gson.toJson(texts));
			List<Texts> list = (List<Texts>) texts.get(valSheetName);
			StringBuffer sb = new StringBuffer();
			
			String result = "PASS";
			for (Texts text: list) {
				String actualText = webElement.findElement(By.xpath(text.getChild())).getText();
				Pattern pattern = Pattern.compile(text.getRegex().replaceAll(" ",""));
				Matcher matcher = pattern.matcher(actualText.replaceAll(" ",""));
				if (matcher.matches()) {
					sb.append("MATCHES. "+ text.getRemark() + "\n");
				}
				else {
					sb.append("NOT MATCH. " +  text.getRemark() + "('"+ actualText + 
							"' vs expected regex '" + text.getRegex() + "') \n");
					result = "FAIL";
				}
			}
			resultReport.setActual(sb.substring(0, sb.length() -1));
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

		
		logger.info("Map:" + gson.toJson(annotation));
		
		LinkedHashMap<?, ?> texts = new LinkedHashMap<>();
		texts = (LinkedHashMap<?, ?>) annotation.get(Texts.annotation);
		
		return execute (
				helper,
				scenario, 
				texts, 
				scenarioID, 
				scenarioSeq);
	}
}
