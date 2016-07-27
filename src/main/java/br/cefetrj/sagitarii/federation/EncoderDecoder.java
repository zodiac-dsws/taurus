package br.cefetrj.sagitarii.federation;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfloat32BE;
import hla.rti1516e.encoding.HLAfloat64BE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAinteger64BE;
import hla.rti1516e.encoding.HLAunicodeString;

public class EncoderDecoder {
	private EncoderFactory encoderFactory;
	
	public HLAunicodeString createHLAunicodeString( String value ) {
		return encoderFactory.createHLAunicodeString( value );
	}
	
	public HLAboolean createHLAboolean( boolean value ) {
		return encoderFactory.createHLAboolean( value );
	}
	
	public HLAfloat32BE createHLAfloat32BE( float value ) {
		return encoderFactory.createHLAfloat32BE( value );
	}
	
	public HLAfloat64BE createHLAfloat64BE( double value ) {
		return encoderFactory.createHLAfloat64BE( value );
	}

	public HLAinteger32BE createHLAinteger32BE( int value ) {
		return encoderFactory.createHLAinteger32BE( value );
	}
	
	public HLAinteger64BE createHLAinteger64BE( long value ) {
		return encoderFactory.createHLAinteger64BE( value );
	}

	public String toString ( byte[] bytes )	{
		HLAunicodeString value = encoderFactory.createHLAunicodeString();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return "";
		}
	}	
	
	public double toFloat64 ( byte[] bytes )	{
		HLAfloat64BE value = encoderFactory.createHLAfloat64BE();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return -1;
		}
	}
	
	public int toInteger32 ( byte[] bytes )	{
		HLAinteger32BE value = encoderFactory.createHLAinteger32BE();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return -1;
		}
	}	

	public long toInteger64 ( byte[] bytes )	{
		HLAinteger64BE value = encoderFactory.createHLAinteger64BE();
		try	{
			value.decode( bytes );
			return value.getValue();
		} catch( DecoderException de )	{
			de.printStackTrace();
			return -1;
		}
	}	

	public boolean toBoolean( byte[] bytes ) {
		HLAboolean value = encoderFactory.createHLAboolean();
		try	{
			value.decode( bytes );
		} catch( DecoderException de )	{
			return false;
		}
		return value.getValue();
	}

	
	public EncoderDecoder() throws Exception {
		encoderFactory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
	}
	
}
