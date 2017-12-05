package my.com.selenex.excel;

public enum SelectorType {
	xpath("xpath"),
	cssSelector("cssSelector"),
	id("id"),
	className("className");

	public String value;
	
	private SelectorType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
