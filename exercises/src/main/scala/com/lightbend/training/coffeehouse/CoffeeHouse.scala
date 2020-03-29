package com.lightbend.training.coffeehouse

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration._
// use ._ to get implicits and use .millis to convert the time

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

  private val finishCoffeeDuration: FiniteDuration =
    context.system.settings.config.getDuration("coffee-house.guest.finish-coffee-duration", TimeUnit.MILLISECONDS).millis
  private val prepareCoffeeDuration: FiniteDuration =
    context.system.settings.config.getDuration("coffee-house.barista.prepare-coffee-duration", TimeUnit.MILLISECONDS).millis

  private val barista: ActorRef = createBarista(prepareCoffeeDuration)
  private val waiter: ActorRef = createWaiter()

  // use context.actorOf to create a child
  // protected members can be accessed only by sub classes in the same package
  protected def createGuest(favouriteCoffee: Coffee, finishCoffeeDuration: FiniteDuration): ActorRef =
    context.actorOf(Guest.props(waiter, favouriteCoffee, finishCoffeeDuration))

  // use system.actorOf() to create a top level actor
  // use context.actorOf() to create a child of this actor
  protected def createBarista(prepareCoffeeDuration: FiniteDuration): ActorRef =
    context.actorOf(Barista.props(prepareCoffeeDuration), "barista")

  protected def createWaiter(): ActorRef = context.actorOf(Waiter.props(barista), "waiter")


  override def receive: Receive = {
    case CreateGuest(favouriteCoffee: Coffee) => createGuest(favouriteCoffee, finishCoffeeDuration)
  }
}
