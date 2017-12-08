package my.com.selenex.vo;

import org.apache.log4j.Logger;

/**
 * 
 * @author Fa'izam
 *
 */
public class Texts {
	static Logger logger = Logger.getLogger(Texts.class);
	
	public static final String annotation = "texts";
	public static final String indicator = "@texts.";
	
	public static final String CHILD = "child";
	public static final String REGEX = "regex";
	public static final String REMARK = "remark";
	
	private String child = null;
	private String regex = null;
	private String remark = null;
	
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
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
	
	/**
	 * 
	 * @param cascade
	 * @return
	 */
	public boolean validateMandatoryFields() {
		if (null == this.getChild()) {
			logger.info("Child cannot been null, " + toString());
			return false;
		}
		if (null == this.getRegex()) {
			logger.info("Regex null, change to '', " + toString());
			this.setRegex("");
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Texts [child=" + child + ", regex=" + regex + ", remark=" + remark + "]";
	}
	
}
