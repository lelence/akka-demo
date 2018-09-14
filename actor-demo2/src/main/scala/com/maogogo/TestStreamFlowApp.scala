package com.maogogo

import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Sink
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.NotUsed
import scala.concurrent.Future
import akka.Done
import akka.stream.scaladsl.Keep

object TestStreamFlowApp extends App {

  implicit val sys = ActorSystem("MySystem")
  implicit val mat = ActorMaterializer()

  /// a1 a2 a3 效果是一样的

  //  a0()

  def a0() {
    val out: Source[Int, NotUsed] = Source(1 to 6)
    val in: Sink[String, Future[Done]] = Sink.foreach(println)
    val f: Flow[String, Int, NotUsed] = Flow.fromSinkAndSource(in, out)

    // Flow.fromSinkAndSourceMat(in, out)(Keep.right)

    // Flow.fromSinkAndSource(in, out)

    // Flow[Int].map(_ * 2)

    // val d = out.via(f).to(in).run()

    // println(d)
  }

  def a1() {
    val source = Source(1 to 6).map(_ * 2)
    source.to(Sink.foreach(println(_))).run()
  }

  def a2() {
    val out = Source(1 to 6)
    val in = Sink.foreach(println)
    val f = Flow[Int].map(_ * 2)
    val d = out.via(f).to(in).run()

    println(d)
  }

  def a3() {
    val otherSink: Sink[Int, NotUsed] =
      Flow[Int].alsoTo(Sink.foreach(println(_))).to(Sink.ignore)

    Source(1 to 6).map(_ * 2).to(otherSink).run()

  }

  //  .via().to()

}