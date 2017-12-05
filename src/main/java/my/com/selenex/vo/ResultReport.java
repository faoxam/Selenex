package my.com.selenex.vo;

import java.util.Date;

public class ResultReport {

	private String scenarioId = "";
	private String caseScenario = "";
	private String action = "";
	private String selectorType = "";
	private String selectorString = "";
	private String description = "";
	private String expected = "";
	private String actual = "";
	private String result = "";
	private Date start = null;
	private Date end = null;
	private String consumingTime;
	
	public String getScenarioId() {
		return scenarioId;
	}
	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}
	
	public String getCaseScenario() {
		return caseScenario;
	}
	public void setCaseScenario(String caseScenario) {
		this.caseScenario = caseScenario;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExpected() {
		return expected;
	}
	public void setExpected(String expected) {
		this.expected = expected;
	}
	
	public String getActual() {
		return actual;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
	public String getConsumingTime() {
		return consumingTime;
	}
	public void setConsumingTime(String consumingTime) {
		this.consumingTime = consumingTime;
	}
	
	@Override
	public String toString() {
		return "ResultReport [scenarioId=" + scenarioId + ", caseScenario=" + caseScenario + ", action=" + action
				+ ", selectorType=" + selectorType + ", selectorString=" + selectorString + ", description="
				+ description + ", expected=" + expected + ", actual=" + actual + ", result=" + result + ", start="
				+ start + ", end=" + end + ", consumingTime=" + consumingTime + "]";
	}
	
	
	
	
	
}
