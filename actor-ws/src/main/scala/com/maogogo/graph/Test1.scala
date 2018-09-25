package com.maogogo.graph

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import akka.{Done, NotUsed}

import scala.concurrent.Future

object Test1 extends App {

  implicit val system = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 5)

  val done: Future[Done] = source.runForeach(println)


  val factorials = source.scan(BigInt(1))((acc, next) ⇒ acc * next)

  val result1: Future[IOResult] =
    factorials
      .map(num ⇒ ByteString(s"$num\n"))
      .runWith(FileIO.toPath(Paths.get("factorials1.txt")))


  val result2: Future[IOResult] =
    factorials.map(_.toString).runWith(lineSink("factorial2.txt"))


  // import system.dispatcher

  // done.onComplete(_ ⇒ system.terminate())


  def lineSink(filename: String): Sink[String, Future[IOResult]] = {
    val fileIO: Sink[ByteString, Future[IOResult]] = FileIO.toPath(Paths.get(""))

    Flow[String]
      .map(s ⇒ ByteString(s + "\n")) // Flow[String, ByteString, NotUsed]
      .toMat(fileIO)(Keep.right)
  }
}
