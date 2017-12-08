package my.com.selenex.vo;

import org.apache.log4j.Logger;

/**
 * 
 * @author Fa'izam
 *
 */
public class Scenario {
	
	static Logger logger = Logger.getLogger(Scenario.class);
	
	public static String XPATH = "xpath";
	public static String CSS = "cssSelector";
	public static String ID = "id";
	public static String CLASSNAME = "className";
	
	private String scenarioTagged;
	private String action;
	private String selectorType;
	private String selectorString;
	private String value;
	private String note;
	
	
	public String getSelectorType() {
		return selectorType;
	}
	public void setSelectorType(String selectorType) {
		this.selectorType = selectorType;
	}
	
	public String getSelectorString() {
		return selectorString;
	}
	public void setSelectorString(String selectorString) {
		this.selectorString = selectorString;
	}

	public String getScenarioTagged() {
		return scenarioTagged;
	}
	public void setScenarioTagged(String scenarioTagged) {
		this.scenarioTagged = scenarioTagged;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public boolean validateMandatoryField() {
		if (null == this.getAction()) {
			logger.info("Action cannot been null, "+ toString());
			return false;
		}
		
		if (null == this.getSelectorType()) {
			logger.info("Selector Type cannot been null, " + toString());
			return false;
		}
		
		if (null == this.getSelectorString()) {
			logger.info("Selector String cannot been null, " + toString());
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "Scenario [scenarioTagged=" + scenarioTagged + ", action=" + action + ", selectorType=" + selectorType
				+ ", selectorString=" + selectorString + ", value=" + value + ", note=" + note + "]";
	}
	
}
