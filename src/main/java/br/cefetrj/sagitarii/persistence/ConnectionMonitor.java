package br.cefetrj.sagitarii.persistence;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionMonitor {
	private static ConnectionMonitor instance;
	private Map<String,Integer> connections;
	private Logger logger;

	public static ConnectionMonitor getinstance() {
		if ( instance == null ) {
			instance = new ConnectionMonitor();
		}
		return instance;
	}
	
	public ConnectionMonitor() {
		connections = new HashMap<String,Integer>();
		logger = LogManager.getLogger( this.getClass().getName() );
	}
	
	public synchronized void releaseMonitor( String className ) {
		logger.debug("release DAO instance monitor for entity " + className);
		Integer quant = connections.get(className);
		if ( quant != null ) {
			quant--;
			logger.debug("DAO instance for entity " + className + " released: " + quant + " instances left open");
			connections.put(className, quant);
		}
	}
	
	public synchronized void startMonitor( String className ) {
		logger.debug("start DAO instance monitor for entity " + className);
		Integer quant = connections.get(className);
		if ( (quant != null) && ( quant > 0 ) ) {
			logger.warn("too much DAO instances for entity " + className);
		} else {
			quant = 0;
		}
		quant++;
		logger.debug( quant + " active DAO instances for "  + className );
		connections.put(className, quant);
	}
	
	
}
