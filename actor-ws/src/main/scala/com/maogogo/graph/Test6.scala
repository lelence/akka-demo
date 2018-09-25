package com.maogogo.graph

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Test6 extends App {

  implicit val system = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()

  val sumSink: Sink[Int, Future[Int]] = Sink.fold(0)(_ + _)

  import Tweet._


  val counterRunnableGraph = //: RunnableGraph[Future[Int]] =
    tweets
      .filter(_.hashtags contains akkaTag)
      .map(_ ⇒ 1)
      .toMat(sumSink)(Keep.right)

  val sum = counterRunnableGraph.run()

  val res = Await.result(sum, Duration.Inf)

  println("res =>>" + res)

  // val sum2: Future[Int] = tweets.map(_ ⇒ 1).runWith(sumSink)

}
