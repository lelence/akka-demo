package com.maogogo.graph

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}

import scala.collection.immutable
import scala.concurrent.Future

object Test10 extends App {

  implicit val system = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()

  import system.dispatcher

  val sinkSeq: immutable.Seq[Sink[String, Future[String]]] = immutable.Seq("a", "b", "c").map { x ⇒
    Flow[String].filter(_.startsWith(x)).toMat(Sink.head[String])(Keep.right)
  }

  val g: RunnableGraph[immutable.Seq[Future[String]]] = RunnableGraph.fromGraph(GraphDSL.create(sinkSeq) {
    implicit b ⇒
      sinks ⇒

        import GraphDSL.Implicits._

        val broadcast = b.add(Broadcast[String](sinks.size))

        Source(List("ax", "bx", "cx")) ~> broadcast

        sinks.foreach(sink ⇒ broadcast ~> sink)

        ClosedShape

  })

  val d = Future.sequence(g.run())

  d.map(_.foreach(println))

}
