package com.maogogo;

import java.net.URI;

import org.java_websocket.handshake.ServerHandshake;

public class MyWebSocketClient extends org.java_websocket.client.WebSocketClient {

	public MyWebSocketClient(URI serverUri) {
		super(serverUri);
	}

	public void onOpen(ServerHandshake handshakedata) {
		// TODO Auto-generated method stub
		
	}

	public void onMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		
	}

	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		
	}

}
