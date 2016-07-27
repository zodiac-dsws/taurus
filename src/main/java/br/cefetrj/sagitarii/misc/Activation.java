package br.cefetrj.sagitarii.misc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Activation implements Comparable<Activation> {
	private Date startTime;
	private Date endTime;
	private int order;
	private String fragment;
	private String experiment;
	private String workflow;
	private String activitySerial;
	private String command;
	private String instanceSerial;
	private List<String> sourceData = new ArrayList<String>();
	private Activation previousActivation;
	private String xmlOriginalData;
	private String type;
	private String executor;
	private String executorType;
	private String targetTable;
	int instanceId;
	
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getFragment() {
		return fragment;
	}
	
	public void setFragment(String fragment) {
		this.fragment = fragment;
	}
	
	public String getActivitySerial() {
		return activitySerial;
	}
	public void setActivitySerial(String serial) {
		this.activitySerial = serial;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getInstanceSerial() {
		return instanceSerial;
	}
	
	public void setInstanceSerial(String instanceSerial) {
		this.instanceSerial = instanceSerial;
	}
	
	@Override
	public int compareTo(Activation pipe) {
		return ( (Integer)pipe.getOrder() ).compareTo( (Integer)order );
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<String> getSourceData() {
		return sourceData;
	}

	public void setSourceData(List<String> sourceData) {
		this.sourceData = sourceData;
	}

	public Activation getPreviousActivation() {
		return previousActivation;
	}

	public void setPreviousActivation(Activation previousActivation) {
		this.previousActivation = previousActivation;
	}

	public String getExperiment() {
		return experiment;
	}

	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public String getXmlOriginalData() {
		return xmlOriginalData;
	}

	public void setXmlOriginalData(String xmlOriginalData) {
		this.xmlOriginalData = xmlOriginalData;
	}

	public String getTargetTable() {
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getExecutorType() {
		return executorType;
	}

	public void setExecutorType(String executorType) {
		this.executorType = executorType;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	
}
