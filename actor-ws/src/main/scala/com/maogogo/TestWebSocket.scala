package com.maogogo

import scala.io.StdIn
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.scaladsl.Flow
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.model.ws.BinaryMessage
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.ws.UpgradeToWebSocket
import akka.http.scaladsl.model.HttpMethods
import scala.concurrent.Future

object TestWebSocket extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val greeterWebSocketService =
    Flow[Message]
      .mapConcat {
        // we match but don't actually consume the text message here,
        // rather we simply stream it back as the tail of the response
        // this means we might start sending the response even before the
        // end of the incoming message has been received
        case tm: TextMessage ⇒
          println("11111===>>" + tm)
          TextMessage(Source.single("Hello ") ++ tm.textStream) :: Nil
        // Future.successful(tm.textStream)
        case bm: BinaryMessage ⇒
          // ignore binary messages but drain content to avoid the stream being clogged
          bm.dataStream.runWith(Sink.ignore)
          Nil
      }

  val requestHandler: HttpRequest ⇒ HttpResponse = {
    case req@HttpRequest(HttpMethods.GET, Uri.Path("/greeter"), _, _, _) ⇒
      req.header[UpgradeToWebSocket] match {
        case Some(upgrade) ⇒
          println("hello ===>> message ==>>" + upgrade)
          // handleWebSocketMessages(greeterWebSocketService)
          upgrade.handleMessages(greeterWebSocketService)
        case None ⇒
          println("ddddddd")
          HttpResponse(400, entity = "Not a valid websocket request!")
      }
    case r: HttpRequest ⇒
      println("aaaaaaaaaaaaaaa")
      r.discardEntityBytes() // important to drain incoming HTTP Entity stream
      HttpResponse(404, entity = "Unknown resource!")
  }

  val bindingFuture =
    Http().bindAndHandleSync(
      requestHandler,
      interface = "localhost",
      port = 8080
    )

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  import system.dispatcher // for the future transformations

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done

}
