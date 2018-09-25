package com.maogogo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.ws.WebSocketRequest
import akka.http.scaladsl.Http

object Test2 extends App {

  //  import org.java_websocket.drafts.Draft
  //  val name = scala.io.StdIn.readLine("What's your name? ")
  //  println("name => " + name)

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  while (true) {
    val name = scala.io.StdIn.readLine("What's your name? ")
    name match {
      case s if "aa".equals(s) ⇒
        System.exit(0)
      case _ ⇒
      // println("name => " + name)
      // name = scala.io.StdIn.readLine("What's your name? ")
    }
  }

  def aa() {
    val (upgradeResponse, closed) =
      Http().singleWebSocketRequest(
        WebSocketRequest("ws://192.168.0.200:8546"),
        null
      )

    //    upgradeResponse.

  }

}
