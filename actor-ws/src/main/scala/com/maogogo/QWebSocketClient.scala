package com.maogogo

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import akka.actor.Actor
import java.net.URI
import org.java_websocket.WebSocket.READYSTATE

case object SendMessage

class QWebSocketClient(uri: URI) extends WebSocketClient(uri) with Actor {

  override def preStart(): Unit = {
    //    connect()
    //    while (!getReadyState().equals(READYSTATE.OPEN)) {
    //      // System.out.println("还没有打开");
    //    }
  }

  override def onClose(x$1: Int, x$2: String, x$3: Boolean): Unit = {
    println("closed")
    // ???
  }

  override def onError(ex: Exception): Unit = {
    // ???
    ex.printStackTrace()
  }

  override def onMessage(message: String): Unit = {
    sender ! message
  }

  override def onOpen(x$1: ServerHandshake): Unit = {
    println("opened")
    //    ???
  }

  def receive: Actor.Receive = {
    case s: String ⇒
      // send(s)

      //      this.
      println("aaa ==>>>" + s)
      sender ! "ssssss"
  }

}
