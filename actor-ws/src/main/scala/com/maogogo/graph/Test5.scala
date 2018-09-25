package com.maogogo.graph

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink}

import scala.concurrent.{Await, Future}

object Test5 extends App {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ ⇒ 1)

  val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  import Tweet._

  val counterGraph: RunnableGraph[Future[Int]] =
    tweets // Source[Tweet, NotUsed]
      .via(count)
      .toMat(sumSink)(Keep.right)


  val sum: Future[Int] = counterGraph.run()

  import scala.concurrent.duration._

  val res = Await.result(sum, Duration.Inf)


  //Source[+Out, +Mat], Flow[-In, +Out, +Mat] and Sink[-In, +Mat]

  println("res => " + res)
}
