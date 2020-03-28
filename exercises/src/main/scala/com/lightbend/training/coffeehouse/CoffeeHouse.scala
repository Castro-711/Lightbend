package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, Props}

object CoffeeHouse {
  // creating a props factory
  def props(): Props = Props(new CoffeeHouse)
}

class CoffeeHouse extends Actor with ActorLogging {
  // when instantiated we want to log
  log.debug("Coffehouse Open")

  override def receive: Receive = {
    case _ => log.info("Coffee Brewing...!")
  }
}
