package br.cefetrj.sagitarii.federation.objects;

import hla.rti1516e.ObjectInstanceHandle;

public class TeapotObject {
	private long freeMemory;		
	private long totalMemory;
	private double cpuLoad;
	private int availableProcessors;
	private String machineName;
	private String soName;
	private String ipAddress;
	private String macAddress;
	private ObjectInstanceHandle instance;

	public boolean isMe( ObjectInstanceHandle objHandle ) {
		if ( instance.equals( objHandle ) ) {
			return true;
		} else return false;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public ObjectInstanceHandle getHandle() {
		return instance;
	}	
	
	public TeapotObject( ObjectInstanceHandle instance ) throws Exception {
		this.instance = instance;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public double getCpuLoad() {
		return cpuLoad;
	}

	public void setCpuLoad(double cpuLoad) {
		this.cpuLoad = cpuLoad;
	}

	public int getAvailableProcessors() {
		return availableProcessors;
	}

	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getSoName() {
		return soName;
	}

	public void setSoName(String soName) {
		this.soName = soName;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	

}
