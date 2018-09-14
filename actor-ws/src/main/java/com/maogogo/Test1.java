package com.maogogo;

import java.io.InputStream;
import java.net.URI;
//import org.java_websocket.drafts.Draft_17;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Test1 {

	public static void main(String[] args) throws Exception {

		WebSocketClient client = new WebSocketClient(new URI("ws://localhost:9000")) {

			public void onOpen(ServerHandshake handshakedata) {
				System.out.println("open...");
			}

			public void onMessage(String message) {
				// TODO Auto-generated method stub
				System.out.println("收到消息" + message);

				// send(message);
			}

			public void onError(Exception ex) {
				// TODO Auto-generated method stub
				ex.printStackTrace();
				System.out.println("发生错误已关闭");

			}

			public void onClose(int code, String reason, boolean remote) {
				System.out.println("链接已关闭");
			}
		};

		client.connect();

		while (!client.getReadyState().equals(READYSTATE.OPEN)) {
			System.out.println("还没有打开");
		}

		
		// client.send("Toan".getBytes("utf-8"));

		java.net.Socket s = client.getSocket();
		
		
		System.out.println(s.getInetAddress());
		System.out.println(s);
		
		
		java.io.OutputStream out = s.getOutputStream();

		
		out.write("hello".getBytes("UTF-8"));
		out.flush();
		
//		out.close();
		
		System.out.println("打开了");

		// InputStream in = s.getInputStream();

		// client.send("Ting");

		// client.

		// System.out.println(client.getResourceDescriptor());

		// System.out.println(client.getAttachment());

		// System.out.println(client.getAttachment());

		// client.close();

	}

}
