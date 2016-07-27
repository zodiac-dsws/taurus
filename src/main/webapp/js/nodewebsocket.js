
function getCoreDiv( core ) {
	var coreDiv = "<tr id=\""+core.serial+"\">" +
	"<td id=\""+core.serial+"_1\" style=\"text-align:left\">"+core.ownerNode+"</td>" +
	"<td id=\""+core.serial+"_3\" style=\"text-align:left\">"+core.experimentSerial+"</td>" +
	"<td id=\""+core.serial+"_4\" style=\"text-align:left\">"+core.executorType+"</td>" +
	"<td id=\""+core.serial+"_5\" style=\"text-align:left\">"+core.executor+"</td>" +
	"<td id=\""+core.serial+"_6\" style=\"text-align:left\">"+core.instanceSerial+"</td>" +
	"<td id=\""+core.serial+"_7\" style=\"text-align:left\">"+core.fragmentSerial+"</td>" +
	"<td id=\""+core.serial+"_8\" style=\"text-align:left\">"+core.activitySerial+"</td>" +
	"</tr>";
	return coreDiv;
}

function addNewCore( core ) {
	var coreExist = $("#" + core.serial);
	if ( !coreExist.length ) {
		var coreDiv = getCoreDiv( core );
		$("#coreTable").append( coreDiv );
	} else {
		updateCore( core );
	}
}

function removeCore( core ) {
	$( "#" + core.serial ).remove();
}

function updateCore( core ) {
	console.log(" >>> " + core.instanceSerial);
	$('#' + core.serial + "_1").html( core.ownerNode );
	$('#' + core.serial + "_3").html( core.experimentSerial );
	$('#' + core.serial + "_4").html( core.executorType );
	$('#' + core.serial + "_5").html( core.executor );
	$('#' + core.serial + "_6").html( core.instanceSerial );
	$('#' + core.serial + "_7").html( core.fragmentSerial );
	$('#' + core.serial + "_8").html( core.activitySerial );
}

function startWebSocket( serverBaseUrl ) {
	
	var mySocket = new WebSocket("ws://"+serverBaseUrl+"websocket/nodes");
	
	mySocket.onopen = function(evt) {
		mySocket.send("WebSocket Rocks!");
	};
	
	// {"objectType":"core","operation":"update","serial":"a9df3","experimentSerial":"7B609BE3-9E6D-4C1","activitySerial":"DA291734","fragmentSerial":"ED5CDAAB",
	//  "ownerNode":"00-13-46-94-18-C1","instanceSerial":"D0F019EE-6241-4"}
	
	mySocket.onmessage = function(event) {
		var data = JSON.parse( event.data );
		var operation = data.operation;
		var type = data.objectType;
		if ( type == 'core' && operation == 'remove' ) {
			if ( data.serial != '' ) {
				removeCore( data );
			}
		}
		if ( type == 'core' && operation == 'update' ) {
			if ( data.serial != '' ) {
				addNewCore ( data );
			}
		}
	};

	mySocket.onclose = function(event) {
	       var reason;
	        if (event.code == 1000)
	            reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
	        else if(event.code == 1001)
	            reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
	        else if(event.code == 1002)
	            reason = "An endpoint is terminating the connection due to a protocol error";
	        else if(event.code == 1003)
	            reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
	        else if(event.code == 1004)
	            reason = "Reserved. The specific meaning might be defined in the future.";
	        else if(event.code == 1005)
	            reason = "No status code was actually present.";
	        else if(event.code == 1006)
	           reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
	        else if(event.code == 1007)
	            reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
	        else if(event.code == 1008)
	            reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
	        else if(event.code == 1009)
	           reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
	        else if(event.code == 1010) // Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
	            reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: " + event.reason;
	        else if(event.code == 1011)
	            reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
	        else if(event.code == 1015)
	            reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
	        else
	            reason = "Unknown reason";
	        
	        console.log("The connection was closed for reason: " + reason);
    };
	
	mySocket.onerror = function(event) {
		//
	};
	
}