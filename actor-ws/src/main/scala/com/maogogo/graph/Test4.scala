package com.maogogo.graph

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, RunnableGraph, Sink}

import scala.concurrent.Future

object Test4 extends App {

  implicit val system = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()

  import Tweet._

  val writeAuthors: Sink[Author, Future[Done]] = Sink.foreach(println)

  val writeHashtags: Sink[Hashtag, Future[Done]] = Sink.foreach(println)

  val g: RunnableGraph[NotUsed] = RunnableGraph.fromGraph(GraphDSL.create() { implicit b ⇒

    import GraphDSL.Implicits._

    val bcast = b.add(Broadcast[Tweet](2))

    tweets ~> bcast.in

    bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors

    bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashtags

    ClosedShape
  })

}
