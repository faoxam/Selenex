package my.com.selenex;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import my.com.selenex.action.Click;

/**
 * 
 * @author Fa'izam
 *
 */
public class SelenexFull {

	static Logger logger = Logger.getLogger(Click.class);
	
	private static String scenarioPath;
	private static String resultPath;
	private static String actionCfg;
	private static JSONObject actions;

	private static int poolSize = 10;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Config cfg = new Config(args[0]);
		scenarioPath = cfg.getProperty("input.path");
		resultPath = cfg.getProperty("output.path");
		actionCfg = cfg.getProperty("action.config");
		poolSize = Integer.parseInt(cfg.getProperty("thread.pool"));

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader(actionCfg));
			actions = (JSONObject) obj;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();

		File folder = new File(scenarioPath);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (!file.isFile() || !file.getAbsolutePath().endsWith(".xls")) {
				logger.info("Skip. File Name:" + file.getAbsolutePath());
				continue;
			}

			Future future = service.submit(new ProcessFullThread(file, resultPath, actions));
			futures.add(future);
		}

		for (Future<Runnable> f : futures) {
			f.get();
		}

		// shut down the executor service so that this thread can exit
		service.shutdownNow();
	}

}
