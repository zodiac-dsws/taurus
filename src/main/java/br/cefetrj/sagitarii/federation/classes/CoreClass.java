package br.cefetrj.sagitarii.federation.classes;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.cefetrj.sagitarii.federation.EncoderDecoder;
import br.cefetrj.sagitarii.federation.RTIAmbassadorProvider;
import br.cefetrj.sagitarii.federation.objects.CoreObject;
import br.cefetrj.sagitarii.websockets.WebSocketClientList;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.RTIexception;

public class CoreClass {
	private RTIambassador rtiamb;
	private ObjectClassHandle classHandle;
	private AttributeHandle isWorkingHandle;
	private AttributeHandle serialNumberHandle;
	private AttributeHandle ownerNodeHandle;
	
	private AttributeHandle experimentSerialHandle;
	private AttributeHandle fragmentSerialHandle;
	private AttributeHandle instanceSerialHandle;
	private AttributeHandle activitySerialHandle;
	private AttributeHandle executorHandle;
	private AttributeHandle executorTypeHandle;
	
	private AttributeHandleSet attributes;
	private List<CoreObject> cores;
	private EncoderDecoder encodec;
	private Logger logger = LogManager.getLogger( this.getClass().getName() );
	
	public List<CoreObject> getCores() {
		return new ArrayList<CoreObject>(cores);
	}

	public CoreObject getCore( String coreSerial ) {
		for ( CoreObject core : getCores()  ) {
			if ( core.getSerial().contentEquals( coreSerial ) ) return core; 
		}
		return null;
	}
	
	public void remove( ObjectInstanceHandle objHandle ) {
		for ( CoreObject core : getCores()  ) {
			if ( core.isMe( objHandle ) ) {
				logger.debug( "Core " + core.getSerial() + " is offline." );
				WebSocketClientList.getInstance().removeCore( core );
				cores.remove( core );
				return;
			}
		}		
	}

	public ObjectInstanceHandle createNew( ObjectInstanceHandle coreObjectHandle ) throws RTIexception {
		CoreObject core = new CoreObject( coreObjectHandle );
		cores.add( core );
		rtiamb.requestAttributeValueUpdate(coreObjectHandle, attributes, "Request Update".getBytes() );
		return coreObjectHandle;
	}
	
	public CoreObject reflectAttributeValues( AttributeHandleValueMap theAttributes, ObjectInstanceHandle theObject ) {
		// Find the Object instance
		for ( CoreObject core : getCores() ) {
			if( core.isMe( theObject) ) {
				// Update its attributes
				for( AttributeHandle attributeHandle : theAttributes.keySet() )	{
					if( attributeHandle.equals( isWorkingHandle) ) {
						core.setWorking(  encodec.toBoolean( theAttributes.get(attributeHandle) ) );
					}
					else if( attributeHandle.equals( experimentSerialHandle ) ) {
						core.setExperimentSerial( encodec.toString( theAttributes.get(attributeHandle)) );
					}
					else if( attributeHandle.equals( fragmentSerialHandle ) ) {
						core.setFragmentSerial( encodec.toString( theAttributes.get(attributeHandle)) );
					}
					else if( attributeHandle.equals( instanceSerialHandle ) ) {
						core.setInstanceSerial( encodec.toString( theAttributes.get(attributeHandle)) );
					}
					else if( attributeHandle.equals( activitySerialHandle ) ) {
						core.setActivitySerial( encodec.toString( theAttributes.get(attributeHandle)) );
					}
					else if( attributeHandle.equals( serialNumberHandle ) ) {
						core.setSerial( encodec.toString( theAttributes.get(attributeHandle)) );
					}
					else if( attributeHandle.equals( ownerNodeHandle ) ) {
						core.setOwnerNode( encodec.toString( theAttributes.get(attributeHandle)) );
					}
					else if( attributeHandle.equals( executorHandle ) ) {
						core.setExecutor( encodec.toString( theAttributes.get(attributeHandle)) );
					}
					else if( attributeHandle.equals( executorTypeHandle ) ) {
						core.setExecutorType( encodec.toString( theAttributes.get(attributeHandle)) );
					}
				}
				/*
				System.out.println(">>>>>>>>>>>>>>>>>>> " + core.getActivitySerial() + " " + core.getExecutor() + 
						" " + core.getExecutorType() + " " + core.getExperimentSerial() + " " + 
						core.getFragmentSerial() + " " + core.getInstanceSerial() + " " + core.getOwnerNode() + " " +
						core.getSerial() );
				*/
				WebSocketClientList.getInstance().updateCore( core );
				return core;
			}
		}
		return null;
	}
	

	
	public boolean objectExists( ObjectInstanceHandle objHandle ) {
		for ( CoreObject object : getCores()  ) {
			if ( object.isMe( objHandle ) ) {
				return true;
			}
		}
		return false;
	}
	
	public ObjectClassHandle getClassHandle() {
		return classHandle;
	}
	
	public boolean isSameOf( ObjectClassHandle theObjectClass ) {
		return theObjectClass.equals( classHandle );
	}
	
	public CoreClass() throws Exception {
		rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();
		this.classHandle = rtiamb.getObjectClassHandle( "HLAobjectRoot.Core" );
		
		this.isWorkingHandle = rtiamb.getAttributeHandle( classHandle, "IsWorking" );
		this.serialNumberHandle = rtiamb.getAttributeHandle( classHandle, "SerialNumber" );
		this.ownerNodeHandle = rtiamb.getAttributeHandle( classHandle, "OwnerNode" );

		this.experimentSerialHandle = rtiamb.getAttributeHandle( classHandle, "ExperimentSerial" );
		this.fragmentSerialHandle = rtiamb.getAttributeHandle( classHandle, "FragmentSerial" );
		this.instanceSerialHandle = rtiamb.getAttributeHandle( classHandle, "InstanceSerial" );
		this.activitySerialHandle = rtiamb.getAttributeHandle( classHandle, "ActivitySerial" );	
		this.executorHandle = rtiamb.getAttributeHandle( classHandle, "Executor" );
		this.executorTypeHandle = rtiamb.getAttributeHandle( classHandle, "ExecutorType" );		
		this.attributes = rtiamb.getAttributeHandleSetFactory().create();
		
		attributes.add( isWorkingHandle );
		attributes.add( serialNumberHandle );
		attributes.add( ownerNodeHandle );
		attributes.add( experimentSerialHandle );
		attributes.add( fragmentSerialHandle );
		attributes.add( instanceSerialHandle );
		attributes.add( activitySerialHandle );
		attributes.add( executorHandle );
		attributes.add( executorTypeHandle );
		
		cores = new ArrayList<CoreObject>();
		encodec = new EncoderDecoder();
	}
	
	public void publish() throws RTIexception {
		logger.debug("publish");
		rtiamb.publishObjectClassAttributes( classHandle, attributes );
	}
	
	public void subscribe() throws RTIexception {
		logger.debug("subscribe");
		rtiamb.subscribeObjectClassAttributes( classHandle, attributes );		
	}

	
	
}
