package com.maogogo

import akka.stream.scaladsl.Source
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import akka.Done
import akka.stream.scaladsl.Sink
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.RunnableGraph
import akka.stream.scaladsl.Keep

final case class Author(handle: String)

final case class Hashtag(name: String)

final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] = body.split(" ").collect {
    case t if t.startsWith("#") ⇒ Hashtag(t.replaceAll("[^#\\w]", ""))
  }.toSet
}

object TestStreamApp extends App {

  val akkaTag = Hashtag("#akka")

  val tweets: Source[Tweet, NotUsed] = Source(
    Tweet(Author("rolandkuhn"), System.currentTimeMillis, "#akka rocks!") ::
      Tweet(Author("patriknw"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("bantonsson"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("drewhk"), System.currentTimeMillis, "#akka !") ::
      Tweet(Author("ktosopl"), System.currentTimeMillis, "#akka on the rocks!") ::
      Tweet(Author("mmartynas"), System.currentTimeMillis, "wow #akka !") ::
      Tweet(Author("akkateam"), System.currentTimeMillis, "#akka rocks!") ::
      Tweet(Author("bananaman"), System.currentTimeMillis, "#bananas rock!") ::
      Tweet(Author("appleman"), System.currentTimeMillis, "#apples rock!") ::
      Tweet(Author("drama"), System.currentTimeMillis, "we compared #apples to #oranges!") ::
      Nil)

  implicit val sys = ActorSystem("QuickStart")

  implicit val mat = ActorMaterializer()

  import sys.dispatcher

  //  val source: Source[Int, NotUsed] = Source(1 to 100)

  //  source.buffer(10, OverflowStrategy.dropHead).runWith(Sink.foreach(println))

  // val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map { x ⇒
    2
  }

  //  val counterGraph: RunnableGraph[Future[Int]] =
  //    tweets
  //      .via(count)
  //      .toMat(sumSink)(Keep.right)
  //
  //  val sum: Future[Int] = counterGraph.run()
  //  sum.foreach(c ⇒ println(s"Total tweets processed: $c"))

  val sumSink = Sink.fold[Int, Int](0)(_ + _)
  val counterRunnableGraph: RunnableGraph[Future[Int]] =
    tweets
      .filter(_.hashtags contains akkaTag)
      .map(t ⇒ 2)
      .toMat(sumSink)(Keep.right)

  val morningTweetsCount: Future[Int] = counterRunnableGraph.run()

  morningTweetsCount.foreach(c ⇒ println(s"Total1 tweets processed: $c"))
  // and once in the evening, reusing the flow
  val eveningTweetsCount: Future[Int] = counterRunnableGraph.run()
  eveningTweetsCount.foreach(c ⇒ println(s"Total2 tweets processed: $c"))

}