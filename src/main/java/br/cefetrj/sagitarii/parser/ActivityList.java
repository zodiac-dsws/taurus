package br.cefetrj.sagitarii.parser;

import java.util.HashMap;
import java.util.Map;

public class ActivityList {
	private Map<String, Activity> activities;
	
	private void adjustList() {
		for (Map.Entry<String, Activity> entry : activities.entrySet() ) {
			Activity activity = entry.getValue();
			for ( String reference : activity.getInputReferences() ) {
				activity.getPreviousActivities().add( activities.get( reference) );
			}
		}
	}
	
	public Map<String, Activity> getActivities() {
		adjustList();
		return activities;
	}
	
	public void putActivity( String reference, Activity activity ) {
		activities.put(reference, activity);
	}
	
	public ActivityList() {
		activities = new HashMap<String,Activity>();
	}
	
}
