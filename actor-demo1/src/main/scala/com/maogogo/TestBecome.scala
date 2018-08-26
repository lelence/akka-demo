package com.maogogo

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

object TestBecome extends App {

  val system = ActorSystem("Test")
  val hot = system.actorOf(Props[HotSwapActor], "hot")

  // import system.dispatcher
  // system.scheduler.scheduleOnce(500 millis) {
  hot ! "bar"
  hot ! "bar"
}

class HotSwapActor extends Actor {

  import context._

  def angry: Receive = {
    case "foo" ⇒ sender() ! "I am already angry?"
    case "bar" ⇒
      println("hahah1")
      become(happy)
  }

  def happy: Receive = {
    case "bar" ⇒
      println("hahah2")
      sender() ! "I am already happy :-)"
    case "foo" ⇒ become(angry)
  }

  def receive = {
    case "foo" ⇒ become(angry)
    case "bar" ⇒
      println("1111111111")
      become(happy)
  }

}