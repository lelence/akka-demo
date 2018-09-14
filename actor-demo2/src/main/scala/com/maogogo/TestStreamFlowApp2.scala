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

object TestStreamFlowApp2 extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // output
  val source: Source[Message, NotUsed] =
    Source.single(TextMessage("""{"jsonrpc":"2.0","method":"eth_syncing","params":[],"id":1}"""))

  // input
  val sink: Sink[Message, Future[String]] = ???

  // val flow: Flow[Message, Message, Boolean] = Flow.fromSinkAndSourceCoupledMat(sink, source)(Keep.right) //(Keep.left)

  //  val d = Source.

  //  val printSink: Sink[Message, Future[Done]] = {
  //    Sink.foreach {
  //      case message: TextMessage.Strict ⇒
  //        println("111 ==>>" + message.text)
  //    }
  //  }

}