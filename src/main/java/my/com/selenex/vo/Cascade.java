/**
 * 
 */
package my.com.selenex.vo;

import org.apache.log4j.Logger;

/**
 * @author Fa'izam
 *
 */
public class Cascade {
	
	static Logger logger = Logger.getLogger(Cascade.class);
	
	public static final String PARENT = "parent tag";
	public static final String CHILD = "child";
	public static final String REGEX = "regex";
	public static final String REMARK = "remark";
	
	private String parent = null;
	private String child = null;
	private String regex = null;
	private String remark = null;
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
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
	
	
	@Override
	public String toString() {
		return "Cascade [parent=" + parent + ", child=" + child + ", regex=" + regex + ", remark=" + remark + "]";
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

		if (null == this.getParent()) {
			logger.info("Parent cannot been null, " + toString());
			return false;
		}
		
		if (null == this.getRegex()) {
			logger.info("Regex null, change to '', " + toString());
			this.setRegex("");
		}
		return true;
	}
}
