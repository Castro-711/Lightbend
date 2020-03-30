package com.lightbend.training.coffeehouse

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.lightbend.training.coffeehouse.Barista.PrepareCoffee

import scala.collection.mutable.Map
import scala.concurrent.duration._
// use ._ to get implicits and use .millis to convert the time

object CoffeeHouse {
  // message protocol for CoffeeHouse
  // message protocol contains messages you can send to the CoffeeHouse
  case class CreateGuest(favouriteCoffee: Coffee)
  case class ApproveCoffee(coffee: Coffee, guest: ActorRef)

  // creating a props factory
  def props(caffeineLimit: Int): Props = Props(new CoffeeHouse(caffeineLimit))
}

class CoffeeHouse(caffeineLimit: Int) extends Actor with ActorLogging {
  // as CreateGuest object comes from CoffeeHouse object
  import CoffeeHouse._
  // when instantiated we want to log
  log.debug("Coffeehouse Open")

  private val finishCoffeeDuration: FiniteDuration =
    context.system.settings.config.getDuration("coffee-house.guest.finish-coffee-duration", TimeUnit.MILLISECONDS).millis
  private val prepareCoffeeDuration: FiniteDuration =
    context.system.settings.config.getDuration("coffee-house.barista.prepare-coffee-duration", TimeUnit.MILLISECONDS).millis

  private val barista: ActorRef = createBarista()
  private val waiter: ActorRef = createWaiter()

  private var guestBook: Map[ActorRef, Int] = Map.empty.withDefaultValue(0)

  // use context.actorOf to create a child
  // protected members can be accessed only by sub classes in the same package
  protected def createGuest(favouriteCoffee: Coffee): ActorRef = {
    context.actorOf(Guest.props(waiter, favouriteCoffee, finishCoffeeDuration))
  }

  // use system.actorOf() to create a top level actor
  // use context.actorOf() to create a child of this actor
  def createBarista(): ActorRef =
    context.actorOf(Barista.props(prepareCoffeeDuration), "barista")

  protected def createWaiter(): ActorRef = context.actorOf(Waiter.props(self), "waiter")


  override def receive: Receive = {
    case CreateGuest(favouriteCoffee: Coffee) =>
      val guest = createGuest(favouriteCoffee)
      guestBook += guest -> 0
      log.info(s"Guest $guest added to guest book")
    case ApproveCoffee(coffee: Coffee, guest: ActorRef) if guestBook(guest) < caffeineLimit =>
        guestBook += guest -> (guestBook(guest) + 1)
        log.info(s"Guest $guest caffeine count incremented.")
        barista.forward(Barista.PrepareCoffee(coffee, guest))
    case ApproveCoffee(coffee: Coffee, guest: ActorRef) =>
        log.info(s"Sorry, $guest, but you have reached your limit")
        context.stop(guest)

  }
}
