package com.lightbend.training.coffeehouse

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object CoffeeHouse {
  // message protocol for CoffeeHouse
  // message protocol contains messages you can send to the CoffeeHouse
  case class CreateGuest(favouriteCoffee: Coffee)

  // creating a props factory
  def props(): Props = Props(new CoffeeHouse)
}

class CoffeeHouse extends Actor with ActorLogging {
  // as CreateGuest object comes from CoffeeHouse object
  import CoffeeHouse._
  // when instantiated we want to log
  log.debug("Coffeehouse Open")

  private val waiter: ActorRef = createWaiter()

  // use context.actorOf to create a child
  // protected members can be accessed only by sub classes in the same package
  protected def createGuest(favouriteCoffee: Coffee): ActorRef =
    context.actorOf(Guest.props(waiter, favouriteCoffee))

  protected def createWaiter(): ActorRef = context.actorOf(Waiter.props(), "waiter")

  override def receive: Receive = {
    case CreateGuest(favouriteCoffee: Coffee) => createGuest(favouriteCoffee)
  }
}
