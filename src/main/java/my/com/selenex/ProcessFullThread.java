package my.com.selenex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import my.com.selenex.driver.WebEventListener;
import my.com.selenex.excel.DataSheet;
import my.com.selenex.util.Helper;
import my.com.selenex.vo.Cascade;
import my.com.selenex.vo.ResultReport;
import my.com.selenex.vo.Scenario;
import my.com.selenex.vo.Table;
import my.com.selenex.vo.Texts;

/**
 * 
 * @author Fa'izam
 *
 */
public class ProcessFullThread implements Runnable {
	
	static Logger logger = Logger.getLogger(ProcessFullThread.class);
	protected File file;
	
	
	private WebDriver webDriver;
	private WebDriverWait wait;
	private EventFiringWebDriver eDriver;
	private WebEventListener eListener;
	private Helper helper;
	private DataSheet ds;
	private Integer scenarioID = 0;
	private JSONObject actions;
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	
	public ProcessFullThread(File file, String resultPath, JSONObject actions) {
		this.file = file;
		this.actions = actions;
		
		logger = Logger.getLogger(file.getName());
		
		//webDriver = new FirefoxDriver();
		ds = new DataSheet(file, resultPath);

		initDriver(ds);
		webDriver.manage().window().maximize();
		
		eDriver = new EventFiringWebDriver(webDriver);
		eListener = new WebEventListener();
		eDriver.register(eListener);
	}

	/**
	 * 
	 * @param ds
	 * @return
	 */
	private WebDriver initDriver(DataSheet ds) {
		
		Map<?, ?> map = ds.readConfig();
		logger.info("Config:" +  gson.toJson(map));
		if (null == map) {
			webDriver = new ChromeDriver();
			return webDriver;
		}
		String browser = (String) map.get("browser");
		String driverPath = (String) map.get("driver_path");
		
		switch (browser) {
			case "firefox":
				System.setProperty("webdriver.gecko.driver", driverPath);
				webDriver = new FirefoxDriver();
				break;
				
			case "safari":
				System.setProperty("webdriver.safari.driver", driverPath);
				webDriver = new SafariDriver();
				break;
				
			case "chrome":
				System.setProperty("webdriver.chrome.driver", driverPath);
				webDriver = new ChromeDriver();
				break;
			
			default:
				break;
		}
		return webDriver;
	}
	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Map<String, Object> annotations = new HashMap<String, Object>();
		try {
			List<String> scenarioSheets = ds.getScenarioSheets();
			logger.info("scenarioSheets:" + scenarioSheets.toString());
			
			for (String scenarioSheet: scenarioSheets) {
				logger.info("Preparing INPUT scenario...");
				List<LinkedHashMap<?,?>> inputList = new ArrayList<LinkedHashMap<?, ?>>();
				inputList = ds.generateInputByScenarioSheet(scenarioSheet);
				
				logger.info("Preparing TABLE validation list...");
				LinkedHashMap<?, ?> columnsToValidateList = new LinkedHashMap<>();
				columnsToValidateList = ds.tableColumnValidationRegex(scenarioSheet);
				annotations.put(Table.annotation, columnsToValidateList);
				
				logger.info("Preparing CASCADES validation list...");
				LinkedHashMap<?, ?> cascadeElements = new LinkedHashMap<>();
				cascadeElements = ds.prepareCascadeValidation(scenarioSheet);
				annotations.put(Cascade.annotation, cascadeElements);
				
				logger.info("Preparing TEXTS validation list...");
				LinkedHashMap<?, ?> multiFields = new LinkedHashMap<>();
				multiFields = ds.prepareMultiFieldsValidation(scenarioSheet);
				annotations.put(Texts.annotation, multiFields);
				
				logger.info("Input :" + gson.toJson(inputList));
				logger.info("Annotation :" + gson.toJson(annotations));
				for (LinkedHashMap<?, ?> input: inputList) {
					List<Scenario> scenarios = ds.loadScenarioByScenarioSheet(scenarioSheet);
					logger.info("scenarios:" + scenarios.toString());
					performActivity(ds, scenarios, input, annotations);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Close driver");
		try {
			logger.info("Save excel result");
			ds.writeOutputToExcel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//eDriver.close();
		//webDriver.close();
	}
	
	/*
	 * @param sc
	 * @param input
	 * @throws Exception 
	 */
	private void performActivity(
			DataSheet ds,
			List<Scenario> scenarios,
			LinkedHashMap<?, ?> input,
			Map<?,?> annotations) throws Exception {

		int scenarioSeq = 0;
		String lastScenarioTag = "";
		for (Scenario scenario: scenarios) {
			if (!scenario.validateMandatoryField()) {
				continue;
			}
			if (!lastScenarioTag.equals(scenario.getScenarioTagged())) {
				lastScenarioTag = scenario.getScenarioTagged();
				scenarioSeq = 0;
				scenarioID++;
				ds.addNewLine("TS-" + scenarioID, scenario.getScenarioTagged());
			}
			scenarioSeq++;
			ResultReport resultReport = new ResultReport();
			
			Map<?,?> actionMap = (Map<?,?>) actions.get(scenario.getAction().toLowerCase());
			String timeout = (String) actionMap.get("timeout");
			wait = new WebDriverWait(webDriver, Integer.parseInt(timeout));
			helper = new Helper(eDriver, webDriver, wait, actions);
			resultReport = helper.executeClass(helper, scenario, input, annotations, scenarioID, scenarioSeq);
			ds.addTestResult(resultReport, scenario.getScenarioTagged());
		}

	}
}
