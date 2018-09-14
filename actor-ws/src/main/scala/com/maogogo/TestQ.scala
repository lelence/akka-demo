package com.maogogo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.actor.Props
import java.net.URI
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.Await

object TestQ extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  implicit val timeout = Timeout(3.seconds)

  val uri = new URI("ws://localhost:9000")
  val a = system.actorOf(Props(classOf[QWebSocketClient], uri), "Test")

  
  val d = Await.result((a ? "haha").mapTo[String], 5 seconds)

  println("d  ==.>>>" + d)
}