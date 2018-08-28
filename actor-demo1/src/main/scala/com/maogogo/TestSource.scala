package com.maogogo

import scala.concurrent.Future
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import akka.stream.ActorMaterializer
import akka.actor.ActorSystem

object TestSource extends App {

  implicit val sys = ActorSystem("aa")
  implicit val mat = ActorMaterializer()
  
  import sys.dispatcher

  val s: Future[scala.collection.immutable.Seq[Int]] = Source.single(1).runWith(Sink.seq)

  s.foreach(s ⇒ println(s"cc ++> ${s}"))

  // s.foreach(list ⇒ println(s"Collected elements: $list")) // prints: Collected elements: List(1)

}