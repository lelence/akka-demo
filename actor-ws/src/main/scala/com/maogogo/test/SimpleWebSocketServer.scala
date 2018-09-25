package com.maogogo.test

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{
  BinaryMessage,
  Message,
  TextMessage,
  UpgradeToWebSocket
}
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.http.scaladsl.settings.ServerSettings
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString

object SimpleWebSocketServer extends App {

  implicit val system = ActorSystem("test")

  implicit val mat = ActorMaterializer()

  val defaultSettings = ServerSettings(system)

  val pingCounter = new AtomicInteger()
  val customWebsocketSettings =
    defaultSettings.websocketSettings
      .withPeriodicKeepAliveData(
        () ⇒ ByteString(s"debug-${pingCounter.incrementAndGet()}")
      )

  val customServerSettings =
    defaultSettings.withWebsocketSettings(customWebsocketSettings)

  val greeterWebSocketService =
    Flow[Message]
      .mapConcat {
        // we match but don't actually consume the text message here,
        // rather we simply stream it back as the tail of the response
        // this means we might start sending the response even before the
        // end of the incoming message has been received
        case tm: TextMessage =>
          TextMessage(Source.single("Hello ") ++ tm.textStream) :: Nil
        case bm: BinaryMessage =>
          // ignore binary messages but drain content to avoid the stream being clogged
          bm.dataStream.runWith(Sink.ignore)
          Nil
      }

  val requestHandler: HttpRequest => HttpResponse = {
    case req@HttpRequest(HttpMethods.GET, Uri.Path("/greeter"), _, _, _) =>
      req.header[UpgradeToWebSocket] match {
        case Some(upgrade) => upgrade.handleMessages(greeterWebSocketService)
        case None =>
          HttpResponse(400, entity = "Not a valid websocket request!")
      }
    case r: HttpRequest =>
      r.discardEntityBytes() // important to drain incoming HTTP Entity stream
      HttpResponse(404, entity = "Unknown resource!")
  }

  val bind = Http().bindAndHandleSync(
    requestHandler,
    interface = "localhost",
    port = 8080,
    settings = customServerSettings
  )

}
