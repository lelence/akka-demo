package com.maogogo.worker

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import com.maogogo.worker.endpoints.RootEndpoints
import akka.actor.Props
import com.maogogo.worker.services.GethClientServiceImpl

object Main extends App {

  implicit val system = ActorSystem("WorkerSystem")

  implicit val mat = ActorMaterializer()

  val geth = system.actorOf(Props(classOf[GethClientServiceImpl], mat), "geth")

  val r = new RootEndpoints(geth)

  Http().bindAndHandle(r(), "0.0.0.0", 9000)

}