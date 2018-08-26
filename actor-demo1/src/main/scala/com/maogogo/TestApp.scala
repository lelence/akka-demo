package com.maogogo

import akka.actor._
import scala.concurrent.duration._

case object Ping
case object Pong

object TestApp extends App {

  val system = ActorSystem("pingpong")
  val pinger = system.actorOf(Props[Pinger], "pinger")
  val ponger = system.actorOf(Props(classOf[Ponger], pinger), "ponger")

  import system.dispatcher
  // system.scheduler.scheduleOnce(500 millis) {
  ponger ! Ping
  // }

  // system.terminate()

}

class Pinger extends Actor {
  var countDown = 100
  def receive: Actor.Receive = {
    case Pong ⇒
      println(s"${self.path} received pong, count down $countDown")

      if (countDown > 0) {
        countDown -= 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }
  }
}

class Ponger(pinger: ActorRef) extends Actor {
  def receive: Actor.Receive = {
    case Ping ⇒
      println(s"${self.path} received ping")
      pinger ! Pong
  }
}