package com.maogogo

import com.github.andyglow.websocket._

object TestWC1 extends App {

  val cli = WebsocketClient[String](
    "wss://stream.binance.com:9443/ws/lrceth@aggTrade"
  ) {
    case str =>
      println(s"<<| $str")
  }

  val ws = cli.open()

  // 5. send messages
  //  ws ! "hello"
  //  ws ! "world"

}
