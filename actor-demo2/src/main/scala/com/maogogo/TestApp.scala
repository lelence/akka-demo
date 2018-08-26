package com.maogogo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import scala.concurrent.Future
import akka.http.scaladsl.model.HttpResponse
import scala.util.Success
import scala.util.Failure
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import akka.util.ByteString
import scala.concurrent.Await
import scala.concurrent.duration._

object TestApp extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val httpReq = HttpRequest(
    method = HttpMethods.POST,
    uri = "http://192.168.0.200:8545",
    entity = HttpEntity(ContentTypes.`application/json`, ByteString("""{"jsonrpc":"2.0","method":"eth_syncing","params":[],"id":1}""")))

  val responseFuture: Future[HttpResponse] = Http().singleRequest(httpReq)

  val d = Await.result(responseFuture, 5 second)

  println(d.discardEntityBytes())

  d.entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body ⇒

    println("Got response, body: " + body.utf8String)
    // log.info("Got response, body: " + body.utf8String)
  }

  // d.entity.dataBytes.map(_.utf8String).runReduce(f)

  //  responseFuture
  //    .onComplete {
  //      case Success(res) ⇒ println(res)
  //      case Failure(_) ⇒ sys.error("something wrong")
  //    }

}

