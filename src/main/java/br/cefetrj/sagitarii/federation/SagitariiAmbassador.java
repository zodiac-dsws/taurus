package br.cefetrj.sagitarii.federation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.cefetrj.sagitarii.federation.federates.SagitariiFederate;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.FederateInternalError;


public class SagitariiAmbassador extends NullFederateAmbassador {
	private Logger logger = LogManager.getLogger( this.getClass().getName() );

	@Override
	public void reflectAttributeValues( ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
	                                    byte[] tag, OrderType sentOrder, TransportationTypeHandle transport,
	                                    SupplementalReflectInfo reflectInfo ) throws FederateInternalError {
		
		reflectAttributeValues( theObject, theAttributes, tag, sentOrder, transport, null, sentOrder, reflectInfo );
			
	}

	
	@Override
	public void reflectAttributeValues( ObjectInstanceHandle theObject,  AttributeHandleValueMap theAttributes,
	                                    byte[] tag,  OrderType sentOrdering, TransportationTypeHandle theTransport,
	                                    LogicalTime time,  OrderType receivedOrdering, SupplementalReflectInfo reflectInfo ) throws FederateInternalError {
		try {
			if ( SagitariiFederate.getInstance().getCoreClass().objectExists( theObject ) ) {
				SagitariiFederate.getInstance().getCoreClass().reflectAttributeValues( theAttributes, theObject );
			} else 
			if ( SagitariiFederate.getInstance().getTeapotClass().objectExists( theObject ) ) {
				SagitariiFederate.getInstance().getTeapotClass().reflectAttributeValues( theAttributes, theObject );
			}
		} catch ( Exception e ) {
			e.printStackTrace(); 
		}
	}
	
	@Override
	public void discoverObjectInstance( ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass, String objectName ) throws FederateInternalError {
		try {
			if ( SagitariiFederate.getInstance().getCoreClass().isSameOf( theObjectClass ) ) {
				try {
					logger.debug("New Core object " + theObject + " discovered (" + objectName + ")");
					SagitariiFederate.getInstance().getCoreClass().createNew( theObject );
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
			
			if ( SagitariiFederate.getInstance().getTeapotClass().isSameOf( theObjectClass ) ) {
				try {
					logger.debug("New Teapot object " + theObject + " discovered (" + objectName + ")");
					SagitariiFederate.getInstance().getTeapotClass().createNew( theObject );
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
			
		} catch ( Exception e ) {
			logger.error( e.getMessage() );
		}
		
	}
	
	@Override
	public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering, SupplementalRemoveInfo removeInfo)	{
		try { 
			if ( SagitariiFederate.getInstance().getTeapotClass().objectExists(theObject) ) {
				try {
					logger.debug("Remove Teapot object " );
					SagitariiFederate.getInstance().getTeapotClass().remove( theObject );
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
			
			
			if ( SagitariiFederate.getInstance().getCoreClass().objectExists(theObject) ) {
				try {
					logger.debug("Remove Core object " );
					SagitariiFederate.getInstance().getCoreClass().remove( theObject );
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
			
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public void receiveInteraction(InteractionClassHandle interactionClass,	ParameterHandleValueMap theParameters,
			byte[] userSuppliedTag, OrderType sentOrdering,	TransportationTypeHandle theTransport, 
			SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
		
		String tag = new String( userSuppliedTag );
		
		try {
			SagitariiFederate sagitarii = SagitariiFederate.getInstance(); 
			
			if ( sagitarii.getRequestTaskInteractionClass().isMe(interactionClass) ) {
				sagitarii.sendInstancesToNode( theParameters );
			}
			
			if ( sagitarii.getFinishedInstanceInteractionClass().isMe(interactionClass) ) {
				sagitarii.finishInstance(theParameters);
			}
			
		} catch ( Exception e ) {
			
		}
		
	}
	
	
	
}
