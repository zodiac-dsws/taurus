package br.cefetrj.sagitarii.websockets;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/*
 *** STRUTS.XML ***
	<constant name="struts.action.excludePattern" value="/websocket/*"/>
*/


@ServerEndpoint(value="/websocket/nodes")
public class NodesWebSocket  {

	@OnOpen
	public void onOpen(Session userSession) {
		WebSocketClientList.getInstance().addClient(userSession);
	}

    @OnClose
    public void onClose(Session userSession) {
    	WebSocketClientList.getInstance().removeClient(userSession);
    }
    
    @OnMessage
    public void onMessage(String message, Session userSession) {
    	//System.out.println("Client talk: " + message );
    	//userSession.getAsyncRemote().sendText( message );
    }
    
}
