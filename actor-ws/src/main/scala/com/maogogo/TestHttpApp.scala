package com.maogogo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.Http
import scala.io.StdIn

object TestHttpApp extends App {

  implicit val actorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer = ActorMaterializer()

  val client = new WebSocketClient()

  val interface = "localhost"
  val port = 8080

  import Directives._

  val route = get {
    pathEndOrSingleSlash {

      onSuccess(client.connected) { resp ⇒

        println("resp =>>" + resp)
        complete(resp)
      }

      // complete("Welcome to websocket server")
    }
  }
  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  StdIn.readLine()

  import actorSystem.dispatcher

  //  actorSystem.s

  // binding.flatMap(_.unbind()).onComplete(_ ⇒ actorSystem.shutdown())
  println("Server is down...")

}