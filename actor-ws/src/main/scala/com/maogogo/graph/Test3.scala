package com.maogogo.graph

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.duration._

object Test3 extends App {

  implicit val system = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 5)

  val factorials = source.scan(BigInt(1))((acc, next) ⇒ acc * next)


  factorials
    .zipWith(source)((num, idx) ⇒ s"$idx! = $num")
    .throttle(1, 1.second)
    .runForeach(println)

}
