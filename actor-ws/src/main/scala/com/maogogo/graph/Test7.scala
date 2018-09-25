package com.maogogo.graph

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Future

object Test7 extends App {

  implicit val system = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()


  // val source: Source[Int, Promise[Option[Int]]] = Source.maybe[Int]

  val matValuePoweredSource =
    Source.actorRef[String](bufferSize = 100, overflowStrategy = OverflowStrategy.fail)

  val (actorRef, source) = matValuePoweredSource.preMaterialize()

  actorRef ! "Hello!"

  val s: Sink[Any, Future[Done]] = Sink.foreach(println)

  source.runWith(s)

}
