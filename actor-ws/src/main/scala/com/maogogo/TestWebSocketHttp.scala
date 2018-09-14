package com.maogogo

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.model.ws.TextMessage
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.BinaryMessage
import akka.util.ByteString

object TestWebSocketHttp extends App {

  implicit val actorSystem = ActorSystem("MyWebSocket")
  implicit val flowMaterializer = ActorMaterializer()

  val echo = new EchoService()

  //  val d = Http().webSocketClientFlow(request, connectionContext, localAddress, settings, log)

  Http().bindAndHandle(echo(), interface = "0.0.0.0", port = 9000)

}

class EchoService {

  def apply(): Route = pathSingleSlash {
    get {
      handleWebSocketMessages(echoService)
      // handleWebsocketMessages(echoService)
    }
  }

  val echoService: Flow[Message, Message, Any] = Flow[Message].map {
    case TextMessage.Strict(txt) ⇒ TextMessage("ECHO: " + txt)
    case BinaryMessage.Strict(txt) ⇒ BinaryMessage(ByteString("hello"))
  }
}

