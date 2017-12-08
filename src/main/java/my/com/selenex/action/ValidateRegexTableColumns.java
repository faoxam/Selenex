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
import my.com.selenex.vo.Table;

/**
 * 
 * @author Fa'izam
 * Validate the table column to have the same REGEX as in expected result
 *
 */
public class ValidateRegexTableColumns {

	static Logger logger = Logger.getLogger(ValidateRegexTableColumns.class);
	
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
			LinkedHashMap<?, ?> annotations, 
			int scenarioID, 
			int scenarioSeq) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		logger.info("Annotations:" + gson.toJson(annotations));
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
			WebElement tbody = helper.findElement(scenario);
			
			List<WebElement> eList = tbody.findElements(By.tagName("tr"));
			//logger.info("Elements (TR): " + tbody.findElements(By.tagName("tr")));
			//Validate all text in columns
			String result = "PASS";
			StringBuffer actualResult = new StringBuffer();
			
			
			
			int idx = scenario.getValue().indexOf(".");
			String valSheetName = scenario.getValue().substring(idx + 1);
			logger.info("valSheetName: " + valSheetName);
			

			
			List<Table> list = (List<Table>) annotations.get(valSheetName);
			int rows = 0;
			for (WebElement e: eList) {
			
				List<WebElement> tdList = e.findElements(By.tagName("td"));
				if (!e.isDisplayed() || !e.isEnabled()) {
					continue;
				}

				int col = 0;
				rows ++;
				for (WebElement td: tdList) {
					if (!td.isDisplayed() || !td.isEnabled()) {
						continue;
					}
					col ++;
					Table table = getByKey(list, col);
					logger.info("Table:: " + table);
					if (null == table) {
						continue;
					}
					
					String text = td.getText();
					String regex = table.getRegex();
					logger.info("Element (TD) -- : " + text);
					logger.info("To validate against regex --: " + regex);
					Pattern pattern = Pattern.compile(regex.replaceAll(" ",""));
					Matcher matcher = pattern.matcher(text.replaceAll(" ",""));
					if (matcher.matches()) {
						actualResult.append("MATCHES. Row = " + rows + ", " + table.getRemark() +"\n");
					}
					else {
						actualResult.append("NOT MATCH. Row = " + rows + ", " +  table.getRemark() + "('"+ td.getText() + 
								"' vs expected regex '" + regex + "') \n");
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
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		logger.info("Map:" + gson.toJson(annotation));
		
		LinkedHashMap<?, ?> column = new LinkedHashMap<>();
		column = (LinkedHashMap<?, ?>) annotation.get(Table.annotation);
		return execute (
				helper,
				scenario, 
				column, 
				scenarioID, 
				scenarioSeq);
	}
	
	/**
	 * 
	 * @param tables
	 * @param key
	 * @return
	 */
	private Table getByKey(List<Table> tables, int key) {
		for (Table table: tables ) {
			if (table.getKey() == key) {
				return table;
			}
		}
		return null;
	}
	
}
