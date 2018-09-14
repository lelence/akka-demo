package com.maogogo

import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.RunnableGraph
import akka.stream.scaladsl.Source
import scala.concurrent.Future
import akka.stream.scaladsl.Keep
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.NotUsed

object TestStreamApp2 extends App {

  implicit val sys = ActorSystem("MySystem")
  implicit val mat = ActorMaterializer()
  import sys.dispatcher

  // Source[Int, Promise[Option[Int]]]
  val source = Source.maybe[Int]

  // output 输出元素
//  val source: Source[Int, NotUsed] = Source(1 to 10)

  // input 输入元素
  val sink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  // connect the Source to the Sink, obtaining a RunnableGraph
  val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

  // materialize the flow and get the value of the FoldSink
  val sum: Future[Int] = runnable.run()

  sum.foreach(c ⇒ println(s"Total1 tweets processed: $c"))

}