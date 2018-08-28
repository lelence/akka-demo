package com.maogogo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.http.scaladsl.model.ws.Message
import scala.concurrent.Future
import akka.Done
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.scaladsl.Source
import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Keep
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.WebSocketRequest
import akka.http.scaladsl.model.StatusCodes
import scala.concurrent.Promise

object TestWebSocketClient extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val printSink: Sink[Message, Future[Done]] = {
    Sink.foreach {
      case message: TextMessage.Strict ⇒
        println(message.text)
    }
  }

  val helloSource: Source[Message, NotUsed] =
    Source.single(TextMessage("hello world!"))

  //  val flow: Flow[Message, Message, Future[Done]] =
  //    Flow.fromSinkAndSourceMat(printSink, helloSource)(Keep.left)

  val flow: Flow[Message, Message, Promise[Option[Message]]] =
    Flow.fromSinkAndSourceMat(
      Sink.foreach[Message](println),
      Source.maybe[Message])(Keep.right)

  val (upgradeResponse, closed) =
    Http().singleWebSocketRequest(WebSocketRequest("ws://localhost:9000/greeter"), flow)

  val connected = upgradeResponse.map { upgrade ⇒
    // just like a regular http request we can access response status which is available via upgrade.response.status
    // status code 101 (Switching Protocols) indicates that server support WebSockets
    if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
      println(upgrade.response.entity.dataBytes)
      Done
    } else {
      throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
    }
  }

  connected.onComplete(println)
  // closed.foreach(_ ⇒ println("closed"))
}