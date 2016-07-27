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

public class FinishedInstanceInteractionClass {
	private InteractionClassHandle finishedInstanceInteractionHandle;
	private RTIambassador rtiamb;
	private ParameterHandle coreSerialNumberParameterHandle;
	private ParameterHandle instanceSerialParameterHandle;
	private EncoderDecoder encodec;
	private Logger logger = LogManager.getLogger( this.getClass().getName() );
	
	public boolean isMe( InteractionClassHandle interactionClass ) {
		return interactionClass.equals( finishedInstanceInteractionHandle ); 
	}
	
	public FinishedInstanceInteractionClass() throws Exception {
		rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();
		finishedInstanceInteractionHandle = rtiamb.getInteractionClassHandle( "HLAinteractionRoot.FinishedInstance" );
		coreSerialNumberParameterHandle = rtiamb.getParameterHandle(finishedInstanceInteractionHandle, "CoreSerialNumber");
		instanceSerialParameterHandle = rtiamb.getParameterHandle(finishedInstanceInteractionHandle, "InstanceSerial");
		encodec = new EncoderDecoder();
	}
	
	public String getNodeSerial( ParameterHandleValueMap parameters ) {
		String coreSerial = encodec.toString( parameters.get( coreSerialNumberParameterHandle ) );
		return coreSerial;
	}
	
	public String getInstanceSerial( ParameterHandleValueMap parameters ) {
		String instanceSerial = encodec.toString( parameters.get( instanceSerialParameterHandle ) );
		return instanceSerial;
	}

	public void publish() throws Exception {
		logger.debug("publish");
		rtiamb.publishInteractionClass( finishedInstanceInteractionHandle );
	}
	
	public void subscribe() throws Exception {
		logger.debug("subcribe");
		rtiamb.subscribeInteractionClass( finishedInstanceInteractionHandle );		
	}
	
	public void send( String targetCore, String instanceSerial ) throws Exception {
		HLAunicodeString coreValue = encodec.createHLAunicodeString( targetCore );
		HLAunicodeString instanceSerialValue = encodec.createHLAunicodeString( instanceSerial );
		ParameterHandleValueMap parameters = rtiamb.getParameterHandleValueMapFactory().create(2);
		parameters.put( coreSerialNumberParameterHandle, coreValue.toByteArray() );
		parameters.put( instanceSerialParameterHandle, instanceSerialValue.toByteArray() );
		rtiamb.sendInteraction( finishedInstanceInteractionHandle, parameters, "Finish Instance".getBytes() );
	}

}
