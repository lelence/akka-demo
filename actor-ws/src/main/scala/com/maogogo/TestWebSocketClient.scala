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
import scala.concurrent.Await

object TestWebSocketClient extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  //  val printSink: Sink[Message, Future[Done]] = {
  //    Sink.foreach {
  //      case message: TextMessage.Strict ⇒
  //        println("111 ==>>" + message.text)
  //    }
  //  }

  val helloSource: Source[Message, NotUsed] =
    Source.single(
      TextMessage(
        """{"jsonrpc":"2.0","method":"eth_syncing","params":[],"id":1}"""
      )
    )

  val s: Sink[Message, Future[Done]] = Sink.foreach[Message](println)
  val d: Source[TextMessage, NotUsed] = Source(
    TextMessage(
      """{"jsonrpc":"2.0","method":"eth_syncing","params":[],"id":1}"""
    ) :: TextMessage(
      """{"jsonrpc":"2.0","method":"eth_syncing","params":[],"id":1}"""
    ) :: Nil
  )

  val flow: Flow[Message, Message, Promise[Option[Message]]] =
    Flow.fromSinkAndSourceMat(
      Sink.foreach[Message](println),
      Source(
        TextMessage(
          """{"jsonrpc":"2.0","method":"eth_syncing","params":[],"id":1}"""
        ) :: TextMessage(
          """{"jsonrpc":"2.0","method":"eth_syncing","params":[],"id":1}"""
        ) :: Nil
      ).concatMat(Source.maybe[Message])(Keep.right)
    )(Keep.right)

  // Flow.fromSinkAndSourceMat(printSink, helloSource)(Keep.left)

  //  val flow: Flow[Message, Message, Promise[Option[Message]]] =
  //    Flow.fromSinkAndSourceMat(
  //      Sink.foreach[Message](println),
  //      Source.maybe[Message])(Keep.right)

  // val (u, d) = Http().singleWebSocketRequest(WebSocketRequest("ws://192.168.0.200:8546"), flow)

  val (upgradeResponse, closed) =
    Http()
      .singleWebSocketRequest(WebSocketRequest("ws://192.168.0.200:8546"), flow)

  import scala.concurrent.duration._

  val connected = upgradeResponse.flatMap { upgrade ⇒
    // just like a regular http request we can access response status which is available via upgrade.response.status
    // status code 101 (Switching Protocols) indicates that server support WebSockets
    if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
      //      val d = Await.result(upgrade.response.entity.dataBytes.map(_.utf8String).runReduce(_ + _), 3 seconds)
      //
      //      println("ddd ==>>>" + d)
      // Done

      upgrade.response.entity.dataBytes.map(_.utf8String).runReduce(_ + _)
    } else {
      throw new RuntimeException(
        s"Connection failed: ${upgrade.response.status}"
      )
    }
  }

  connected.onComplete(println)
  // closed.foreach(_ ⇒ println("closed"))
}
