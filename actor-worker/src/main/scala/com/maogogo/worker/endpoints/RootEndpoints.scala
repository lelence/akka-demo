package com.maogogo.worker.endpoints

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.maogogo.worker.services.GethClientServiceImpl
import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

class RootEndpoints(geth: ActorRef) {

  implicit val timeout = Timeout(5 seconds)
  
  def apply(): Route = {
    pathEndOrSingleSlash {
      onSuccess((geth ? "Toan").mapTo[String]) { resp ⇒
        println("resp ==>>" + resp)
        complete("Ok: " + resp)
      }
    }
  }

}