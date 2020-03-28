package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object CoffeeHouse {
  // message protocol for CoffeeHouse
  // message protocol contains messages you can send to the CoffeeHouse
  case object CreateGuest

  // creating a props factory
  def props(): Props = Props(new CoffeeHouse)
}

class CoffeeHouse extends Actor with ActorLogging {
  // as CreateGuest object comes from CoffeeHouse object
  import CoffeeHouse._
  // when instantiated we want to log
  log.debug("Coffeehouse Open")

  // use context.actorOf to create a child
  // protected members can be accessed only by sub classes in the same package
  protected def createGuest(): ActorRef = context.actorOf(Guest.props())

  override def receive: Receive = {
    case CreateGuest => createGuest()
  }
}
