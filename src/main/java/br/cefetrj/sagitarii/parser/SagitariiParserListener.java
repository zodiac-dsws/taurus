package br.cefetrj.sagitarii.parser;

import java.util.Map;

import br.cefetrj.sagitarii.parser.SagitariiParser.ActivitySentenceContext;
import br.cefetrj.sagitarii.parser.SagitariiParser.InputSentenceContext;

public class SagitariiParserListener extends SagitariiBaseListener {
	private ActivityList list = new ActivityList();

	public void showList() {
		for (Map.Entry<String, Activity> entry : list.getActivities().entrySet() )	{
			Activity activity = entry.getValue();
			String entrance = "";
			if ( activity.isEntrance() ) {
				entrance = "entrance";
			}
		    System.out.println( activity.getName() +  " (" + activity.getOperator() + ") - " + entrance );
		    for ( String s : activity.getInputRelations() ) {
			    System.out.println(" Input : " + s);
		    }
		    for ( Activity act : activity.getPreviousActivities() ) {
				System.out.println(" Input: " + act.getTargetRelation() + " (" + act.getName() + ")");
			}
		    
		    System.out.println(" Output: " + activity.getTargetRelation() );
		}
	}
	
	@Override 
	public void enterActivitySentence( ActivitySentenceContext ctx ) { 
		Activity act = new Activity();
		System.out.println( "START NEW ACTIVITY: " + ctx.getText() );
		
		//System.out.println(" >> " + ctx.executor().getText() );
		//System.out.println(" >> " + ctx.output_relation().getText() );
		//System.out.println(" >> " + ctx.activity_name().getText() );
		//System.out.println(" >> " + ctx.OPERATOR().getText() );
		
		for ( InputSentenceContext isc : ctx.inputSentence() ) {
			if( isc.activitySentence() != null ) {
				act.getInputReferences().add( isc.activitySentence().getText() );
				System.out.println(" >> AS " + isc.activitySentence().getText() );
			}
			if( isc.input_relation() != null ) {
				act.getInputRelations().add( isc.input_relation().getText() );
				//System.out.println(" >> IR " + isc.input_relation().getText() );
			}
		}
		
		act.setExecutor( ctx.executor().getText() );
		act.setName( ctx.activity_name().getText() );
		act.setOperator( ctx.OPERATOR().getText() );
		act.setTargetRelation( ctx.output_relation().getText() );
		
		list.putActivity(ctx.getText(), act);
		
		System.out.println("");
	}
	

}
