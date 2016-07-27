package br.cefetrj.sagitarii.websockets;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.Session;

import br.cefetrj.sagitarii.federation.objects.CoreObject;


public class WebSocketClientList {
	private static WebSocketClientList instance;
	private Set<Session> userSessions = Collections.synchronizedSet( new HashSet<Session>() );
	
	public static WebSocketClientList getInstance() {
		if ( instance == null ) {
			instance = new WebSocketClientList();
		}
		return instance;
	}
	
	public void addClient( Session userSession ) {
		userSession.setMaxTextMessageBufferSize(32768);
		userSession.setMaxBinaryMessageBufferSize(32768);
		userSessions.add(userSession);		
	}
	
	public void removeClient( Session userSession ) {
		userSessions.remove(userSession);
	}
	
	private synchronized void message( String message ) {
        for (Session session : userSessions) {
        	try {
        		session.getAsyncRemote().sendText( message );
        	} catch ( Exception e ) { }
        }		
	}
	
	public void updateCore( CoreObject core ) {
		message( coreToJson(core,"update") );
	}

	public void removeCore( CoreObject core ) {
		message( coreToJson(core,"remove") );
	}
	
	/*
	public void updateNode( TeapotObject node ) {
		message( nodeToJson(node,"update") );
	}
	
	public void removeNode( TeapotObject node ) {
		message( nodeToJson(node,"remove") );
	}
	*/
	
	private String coreToJson( CoreObject core, String operation ) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append( generateJsonPair("objectType", "core") + "," );
		sb.append( generateJsonPair("operation", operation) + "," ); 
		sb.append( generateJsonPair("serial", core.getSerial()) + "," ); 
		sb.append( generateJsonPair("experimentSerial", core.getExperimentSerial()) + "," ); 
		sb.append( generateJsonPair("activitySerial", core.getActivitySerial()) + "," ); 
		sb.append( generateJsonPair("fragmentSerial", core.getFragmentSerial()) + "," ); 
		sb.append( generateJsonPair("ownerNode", core.getOwnerNode() ) + "," );
		sb.append( generateJsonPair("executor", core.getExecutor() ) + "," );
		sb.append( generateJsonPair("executorType", core.getExecutorType() ) + "," );
		//sb.append( generateJsonPair("working", isWorking ) + "," );
		sb.append( generateJsonPair("instanceSerial", core.getInstanceSerial() )  ); 
		sb.append("}");		
		return sb.toString();
	}
	
	/*
	private String nodeToJson( TeapotObject node, String operation ) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append( generateJsonPair("objectType", "node") + "," );
		sb.append( generateJsonPair("operation", operation) + "," ); 
		sb.append( generateJsonPair("macAddress", node.getMacAddress() ) + "," ); 
		sb.append( generateJsonPair("ipAddress", node.getIpAddress() ) + "," ); 
		sb.append( generateJsonPair("machineName", node.getMachineName() ) + "," ); 
		sb.append( generateJsonPair("cores", String.valueOf( node.getAvailableProcessors() ) ) + "," ); 
		sb.append( generateJsonPair("cpuLoad", String.valueOf( node.getCpuLoad() ) ) + "," );
		sb.append( generateJsonPair("freeMemory", String.valueOf( node.getFreeMemory() ) ) + "," );
		sb.append( generateJsonPair("totalMemory", String.valueOf( node.getTotalMemory() ) ) ); 
		sb.append("}");		
		return sb.toString();
	}	
	*/
	
	private String generateJsonPair(String paramName, String paramValue) {
		if( paramValue.equals("*") ) {
			paramValue = "";
		}
		return "\"" + paramName + "\":\"" + paramValue + "\""; 
	}

	/*
	private String addArray(String paramName, String arrayValue) {
		return "\"" + paramName + "\":" + arrayValue ; 
	}
	*/	
	
}
