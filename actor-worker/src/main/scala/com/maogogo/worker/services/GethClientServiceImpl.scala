package com.maogogo.worker.services

import akka.actor.Actor
import scala.concurrent.Future
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.http.scaladsl.model.ws.TextMessage
import akka.Done
import akka.stream.scaladsl.Source
import akka.http.scaladsl.model.ws.Message
import akka.NotUsed
import akka.http.scaladsl.model.ws.WebSocketRequest
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.Http
import akka.stream.scaladsl.Keep
import akka.http.scaladsl.model.StatusCodes
import scala.concurrent.Promise

class GethClientServiceImpl(implicit mat: ActorMaterializer) extends Actor {

  import context.dispatcher

  implicit val sys = context.system

  lazy val printSink: Sink[Message, Future[Done]] = {

    Sink.foreach {
      case message: TextMessage.Strict ⇒
        println(message.text)
    }
  }

  lazy val helloSource: Source[Message, NotUsed] = {
    Source.single(TextMessage("Test"))
  }

  // the Future[Done] is the materialized value of Sink.foreach
  // and it is completed when the stream completes
  lazy val flow: Flow[Message, Message, Future[Done]] = {
    Flow.fromSinkAndSourceMat(printSink, helloSource)(Keep.left)
  }

  //  val flow: Flow[Message, Message, Promise[Option[Message]]] =
  //    Flow.fromSinkAndSourceMat(
  //      Sink.foreach[Message](println),
  //      Source.maybe[Message])(Keep.right)

  // upgradeResponse is a Future[WebSocketUpgradeResponse] that
  // completes or fails when the connection succeeds or fails
  // and closed is a Future[Done] representing the stream completion from above

  //  val d = Http().singleWebSocketRequest(request, clientFlow)

  lazy val (upgradeResponse, closed) =
    Http().singleWebSocketRequest(WebSocketRequest("ws://localhost:8080/greeter"), flow)

  lazy val connected = upgradeResponse.flatMap { upgrade ⇒
    // just like a regular http request we can access response status which is available via upgrade.response.status
    // status code 101 (Switching Protocols) indicates that server support WebSockets
    if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
      println("=================")
      upgrade.response.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
    } else {
      throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
    }
  }

  def receive: Actor.Receive = {
    case s: String ⇒

      val dd = connected

      dd.onComplete(println)

      dd pipeTo sender
  }

}