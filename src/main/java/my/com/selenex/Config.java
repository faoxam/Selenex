package my.com.selenex;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
	Properties configFile;

	public Config() {
		configFile = new java.util.Properties();
		try {
			configFile.load(new FileInputStream("configuration/selenex.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Config(String propertiesFile) {
		configFile = new java.util.Properties();
		try {
			configFile.load(new FileInputStream(propertiesFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		String value = this.configFile.getProperty(key);
		return value;
	}
	
	
}
