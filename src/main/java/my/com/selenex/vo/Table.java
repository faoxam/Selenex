package my.com.selenex.vo;

import org.apache.log4j.Logger;

/**
 * 
 * @author Fa'izam
 *
 */
public class Table {
	
	static Logger logger = Logger.getLogger(Table.class);
	
	public static final String KEY = "key";
	public static final String REGEX = "regex";
	public static final String REMARK = "remark";
	
	private int key = -1;
	private String regex;
	private String remark;
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	@Override
	public String toString() {
		return "Table [key=" + key + ", regex=" + regex + ", remark=" + remark + "]";
	}
	public boolean validateMandatoryFields() {
		if (this.getKey() == -1) {
			logger.info("Key was not being set");
			return false;
		}
		
		if (null == this.getRegex()) {
			logger.info("Regex null, change to ''");
			this.setRegex("");
		}
		return true;
	}
	
	
	

}
