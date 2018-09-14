package com.maogogo;

import java.net.URI;

public class Test3 {

	public static void main(String[] args) throws Exception {
		MyWebSocketClient client = new MyWebSocketClient(new URI(""));
		
		client.send("");
		

	}

}
