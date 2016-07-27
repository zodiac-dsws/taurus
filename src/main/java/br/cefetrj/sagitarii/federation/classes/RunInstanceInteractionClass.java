package br.cefetrj.sagitarii.federation.classes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.cefetrj.sagitarii.federation.EncoderDecoder;
import br.cefetrj.sagitarii.federation.RTIAmbassadorProvider;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.HLAunicodeString;

public class RunInstanceInteractionClass {
	private InteractionClassHandle runInstanceInteractionHandle;
	private ParameterHandle instanceContentParameterHandle;
	private ParameterHandle coreSerialNumberParameterHandle;
	private EncoderDecoder encodec;
	private Logger logger = LogManager.getLogger( this.getClass().getName() );
	
	private RTIambassador rtiamb;

	public boolean isMe( InteractionClassHandle interactionClass ) {
		return interactionClass.equals( runInstanceInteractionHandle ); 
	}
	
	
	public RunInstanceInteractionClass() throws Exception {
		rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();
		runInstanceInteractionHandle = rtiamb.getInteractionClassHandle( "HLAinteractionRoot.RunInstance" );
		instanceContentParameterHandle = rtiamb.getParameterHandle(runInstanceInteractionHandle, "InstanceContent");
		coreSerialNumberParameterHandle = rtiamb.getParameterHandle(runInstanceInteractionHandle, "CoreSerialNumber");
		encodec = new EncoderDecoder();
	}
	
	public void publish() throws Exception {
		logger.debug("publish");
		rtiamb.publishInteractionClass( runInstanceInteractionHandle );
	}
	
	public void subscribe() throws Exception {
		logger.debug("subscribe");
		rtiamb.subscribeInteractionClass( runInstanceInteractionHandle );		
	}
	
	public void send( String instance, String targetCore ) throws Exception {
		HLAunicodeString instanceValue = encodec.createHLAunicodeString( instance );
		HLAunicodeString coreValue = encodec.createHLAunicodeString( targetCore );
		
		ParameterHandleValueMap parameters = rtiamb.getParameterHandleValueMapFactory().create(2);
		parameters.put( instanceContentParameterHandle, instanceValue.toByteArray() );
		parameters.put( coreSerialNumberParameterHandle, coreValue.toByteArray() );
		rtiamb.sendInteraction( runInstanceInteractionHandle, parameters, "Run Instance".getBytes() );		
	}
	
}
