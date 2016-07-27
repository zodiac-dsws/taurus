package br.cefetrj.sagitarii.federation.objects;

import hla.rti1516e.ObjectInstanceHandle;

public class SagitariiObject {
	private ObjectInstanceHandle instance;
	private String macAddress;
	
	public boolean isMe( ObjectInstanceHandle objHandle ) {
		if ( instance.equals( objHandle ) ) {
			return true;
		} else return false;
	}
	
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public SagitariiObject( ObjectInstanceHandle instance ) {
		this.instance = instance; 
		macAddress = "10:10:20:20:30:40";
	}
	
	public ObjectInstanceHandle getHandle() {
		return instance;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
	
}
