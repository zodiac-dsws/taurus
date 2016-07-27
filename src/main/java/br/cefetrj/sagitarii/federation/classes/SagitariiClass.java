package br.cefetrj.sagitarii.federation.classes;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.RTIexception;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.cefetrj.sagitarii.federation.EncoderDecoder;
import br.cefetrj.sagitarii.federation.RTIAmbassadorProvider;
import br.cefetrj.sagitarii.federation.objects.SagitariiObject;


public class SagitariiClass {
	private static ObjectClassHandle classHandle;
	private AttributeHandle macAddressAttributeHandle;
	private EncoderDecoder encodec;
	private AttributeHandleSet attributes;
	private RTIambassador rtiamb;
	private Logger logger = LogManager.getLogger( this.getClass().getName() );

	private List<SagitariiObject> objects;
	
	public SagitariiObject createNew() throws RTIexception {
		logger.debug("new HLA Object instance");
		ObjectInstanceHandle handle = rtiamb.registerObjectInstance( classHandle, "Sagitarii Server" );
		SagitariiObject object = new SagitariiObject( handle );
		objects.add( object );
		return object;
	}	
	
	public SagitariiObject createNew( ObjectInstanceHandle objectHandle ) throws RTIexception {
		SagitariiObject object = new SagitariiObject( objectHandle );
		objects.add( object );
		return object;
	}
	
	
	public SagitariiObject reflectAttributeValues( AttributeHandleValueMap theAttributes, ObjectInstanceHandle theObject ) {
		// Find the Object instance
		for ( SagitariiObject object : objects ) {
			if( object.isMe( theObject) ) {
				// Update its attributes
				for( AttributeHandle attributeHandle : theAttributes.keySet() )	{
					if( attributeHandle.equals( macAddressAttributeHandle ) ) {
						object.setMacAddress( encodec.toString( theAttributes.get(attributeHandle) ) );
					}
				}
				return object;
			}
		}
		return null;
	}
	
	
	public SagitariiClass() throws Exception {
		objects = new ArrayList<SagitariiObject>();
		
		logger.debug("new server");
		rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();

		classHandle = rtiamb.getObjectClassHandle( "HLAobjectRoot.SagitariiServer" );
		attributes = rtiamb.getAttributeHandleSetFactory().create();
		encodec = new EncoderDecoder();
		
		logger.debug("registering attributes");
		macAddressAttributeHandle = rtiamb.getAttributeHandle( classHandle, "MACAddress" );
		attributes.add( macAddressAttributeHandle );
	}
	
	public boolean objectExists( ObjectInstanceHandle objHandle ) {
		for ( SagitariiObject object : objects  ) {
			if ( object.isMe( objHandle ) ) {
				return true;
			}
		}
		return false;
	}
	
	public void updateAttributeValues() throws RTIexception {
		logger.debug("updating attributes");
		
		for ( SagitariiObject object : objects ) {
			String macAddress = object.getMacAddress();
			ObjectInstanceHandle objectInstanceHandle = object.getHandle();
			AttributeHandleValueMap attributes = rtiamb.getAttributeHandleValueMapFactory().create(1);
			HLAunicodeString macAddressValue = encodec.createHLAunicodeString( macAddress );
			attributes.put( macAddressAttributeHandle, macAddressValue.toByteArray() );
			rtiamb.updateAttributeValues( objectInstanceHandle, attributes, "Sagitarii Server Attributes".getBytes() );
		}
		
	}
	
	public ObjectClassHandle getClassHandle() {
		return classHandle;
	}
	
	public boolean isSameOf( ObjectClassHandle theObjectClass ) {
		return theObjectClass.equals( classHandle );
	}
	
	public void publish() throws RTIexception {
		logger.debug("publish");
		rtiamb.publishObjectClassAttributes( classHandle, attributes );
	}
	
	public void subscribe() throws RTIexception {

	}	

}
