package com.maogogo.graph

import akka.Done
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

object Test2 extends App {

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  import Tweet._

  val sink: Sink[Any, Future[Done]] = Sink.foreach(println)

  val d: Future[Done] = tweets
    .map(_.hashtags) // Source[Set[Hashtag], NotUsed] Get all sets of hashtags ...
    .reduce(_ ++ _) // Source[Set[Hashtag], NotUsed] ... and reduce them to a single set, removing duplicates across all tweets
    .mapConcat(identity) // Source[Hashtag, NotUsed] Flatten the stream of tweets to a stream of hashtags
    .map(_.name.toUpperCase) // Source[String, NotUsed] Convert all hashtags to upper case
    .runWith(sink)


}


