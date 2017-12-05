package my.com.selenex;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import my.com.selenex.action.Click;

public class App {
	static Logger logger = Logger.getLogger(Click.class);
	
	private String scenarioPath;
	private String resultPath;
	private String actionCfg;
	private JSONObject actions;
	
	private int poolSize = 10;

	@BeforeClass
	public void setup() {
		Config cfg = new Config();
		scenarioPath = cfg.getProperty("input.path");
		resultPath = cfg.getProperty("output.path");
		actionCfg = cfg.getProperty("action.config");
		
		logger.info("scenarioPath:" + scenarioPath);
		logger.info("resultPath:" + resultPath);
		logger.info("actionCfg:" + actionCfg);
		
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader(actionCfg));
			actions = (JSONObject) obj;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
    }
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void executeTest() throws Exception {

		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();

		File folder = new File(scenarioPath);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (!file.isFile() || !file.getAbsolutePath().endsWith(".xls")) {
				logger.info("Skip. File Name:" + file.getAbsolutePath());
				continue;
			}

			Future future = service.submit(new ProcessThread(file, resultPath, actions));
			futures.add(future);
		}

		for (Future<Runnable> f : futures) {
			f.get();
		}

		// shut down the executor service so that this thread can exit
		service.shutdownNow();
	}
	
	
}
