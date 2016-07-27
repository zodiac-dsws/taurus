package br.cefetrj.sagitarii.federation.classes;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.cefetrj.sagitarii.federation.EncoderDecoder;
import br.cefetrj.sagitarii.federation.RTIAmbassadorProvider;
import br.cefetrj.sagitarii.federation.objects.TeapotObject;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.RTIexception;

public class TeapotClass {
	private ObjectClassHandle classHandle;
	private RTIambassador rtiamb;
	
	private AttributeHandle macAddressHandle;
	private AttributeHandle soNameHandle;
	private AttributeHandle machineNameHandle;
	private AttributeHandle cpuLoadHandle;
	private AttributeHandle availableProcessorsHandle;
	private AttributeHandle totalMemoryHandle;
	private AttributeHandle freeMemoryHandle;
	private AttributeHandle ipAddressHandle;
	
	private AttributeHandleSet attributes;
	
	private EncoderDecoder encodec;	
	private List<TeapotObject> nodes;
	private Logger logger = LogManager.getLogger( this.getClass().getName() );

	public List<TeapotObject> getNodes() {
		return new ArrayList<TeapotObject>( nodes );
	}
	
	public boolean objectExists( ObjectInstanceHandle objHandle ) {
		for ( TeapotObject teapot : getNodes()  ) {
			if ( teapot.isMe( objHandle ) ) {
				return true;
			}
		}
		return false;
	}

	public void remove( ObjectInstanceHandle objHandle ) {
		for ( TeapotObject node : getNodes()  ) {
			if ( node.isMe(objHandle ) ) {
				logger.debug( "Node " + node.getMacAddress() + " is offline." );
				nodes.remove( node );
				return;
			}
		}		
	}
	
	public TeapotObject createNew( ObjectInstanceHandle objectHandle ) throws Exception {
		logger.debug("discovered new Teapot node");
		TeapotObject node = new TeapotObject( objectHandle );
		nodes.add( node );
		return node;
	}
	
	public ObjectClassHandle getClassHandle() {
		return classHandle;
	}
	
	public boolean isSameOf( ObjectClassHandle theObjectClass ) {
		return theObjectClass.equals( classHandle );
	}
	
	public TeapotClass( ) throws Exception {
		nodes = new ArrayList<TeapotObject>();
		
		rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();
		this.classHandle = rtiamb.getObjectClassHandle( "HLAobjectRoot.Teapot" );
		
		this.macAddressHandle = rtiamb.getAttributeHandle( classHandle, "MACAddress" );
		this.soNameHandle = rtiamb.getAttributeHandle( classHandle, "SOName" );
		this.machineNameHandle = rtiamb.getAttributeHandle( classHandle, "MachineName" );
		this.cpuLoadHandle = rtiamb.getAttributeHandle( classHandle, "CpuLoad" );
		this.availableProcessorsHandle = rtiamb.getAttributeHandle( classHandle, "AvailableProcessors" );
		this.totalMemoryHandle = rtiamb.getAttributeHandle( classHandle, "TotalMemory" );
		this.freeMemoryHandle = rtiamb.getAttributeHandle( classHandle, "FreeMemory" );
		this.ipAddressHandle = rtiamb.getAttributeHandle( classHandle, "IPAddress" );
		
		this.attributes = rtiamb.getAttributeHandleSetFactory().create();
		attributes.add( macAddressHandle );
		attributes.add( soNameHandle );
		attributes.add( machineNameHandle );
		attributes.add( cpuLoadHandle );
		attributes.add( availableProcessorsHandle );
		attributes.add( totalMemoryHandle );
		attributes.add( freeMemoryHandle );
		attributes.add( ipAddressHandle );
		
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

	public TeapotObject reflectAttributeValues( AttributeHandleValueMap theAttributes, ObjectInstanceHandle theObject ) {
		// Find the Object instance
		for ( TeapotObject node : getNodes() ) {
			
			if( node.getHandle().equals( theObject) ) {
				// Update its attributes
				for( AttributeHandle attributeHandle : theAttributes.keySet() )	{	
					
					if( attributeHandle.equals( macAddressHandle) ) {
						node.setMacAddress( encodec.toString( theAttributes.get(attributeHandle) ) );
					} else
					if( attributeHandle.equals( soNameHandle) ) {
						node.setSoName( encodec.toString( theAttributes.get(attributeHandle) ) );
					} else 
					if( attributeHandle.equals( machineNameHandle) ) {
						node.setMachineName( encodec.toString( theAttributes.get(attributeHandle) ) );
					} else
					if( attributeHandle.equals( cpuLoadHandle) ) {
						node.setCpuLoad( encodec.toFloat64( theAttributes.get(attributeHandle) ) );
					} else  
					if( attributeHandle.equals( availableProcessorsHandle) ) {
						node.setAvailableProcessors( encodec.toInteger32( theAttributes.get(attributeHandle) ) );
					} else 
					if( attributeHandle.equals( totalMemoryHandle) ) {
						node.setTotalMemory( encodec.toInteger64( theAttributes.get(attributeHandle) ) );
					} else 
					if( attributeHandle.equals( freeMemoryHandle) ) {
						node.setFreeMemory( encodec.toInteger64( theAttributes.get(attributeHandle) ) );
					} else
					if( attributeHandle.equals( ipAddressHandle) ) {
						node.setIpAddress( encodec.toString( theAttributes.get(attributeHandle) ) );
					}  
				}
				return node;
			}
		}
		return null;
	}
	
}
