package com.maogogo.ws

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, WebSocketRequest, WebSocketUpgradeResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow

import scala.concurrent.Future

object Test1 extends App {

  implicit val system = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()

  val d: Flow[Message, Message, Future[WebSocketUpgradeResponse]] =
    Http().webSocketClientFlow(WebSocketRequest("ws://echo.websocket.org"))


  val f: Flow[Message, Message, Future[Done]] = ???

  val e: (Future[WebSocketUpgradeResponse], Future[Done]) =
    Http().singleWebSocketRequest(WebSocketRequest("ws://echo.websocket.org"), f)

}
