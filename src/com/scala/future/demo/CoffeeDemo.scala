package com.scala.future.demo
import scala.util.Random
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure,Success}
/**
  * Created by Honda on 2017/11/10.
  */
object CoffeeDemo {
  type CoffeeBeans = String
  type GroundCoffee = String
  case class Water(temperature: Int)
  type Milk = String
  type FrothedMilk = String
  type Espresso = String
  type Cappuccino = String

  case class GrindingException(msg: String) extends Exception(msg)
  case class FrothingException(msg: String) extends Exception(msg)
  case class WaterBoilingException(msg: String) extends Exception(msg)
  case class BrewingException(msg: String) extends Exception(msg)

  def main(args: Array[String]): Unit = {
    grind("baked beans").onComplete {
      case Success(ground) => println(s"got my $ground")
      case Failure(ex) => println("This grinder needs a replacement, seriously!")
    }

    val tempreatureOkay: Future[Boolean] = heatWater(Water(25)) map { water =>
      println("we're in the future!")
      (80 to 85) contains (water.temperature)
    }

    val nestedFuture: Future[Future[Boolean]] = heatWater(Water(25)).map {
      water => temperatureOkay(water)
    }

    val flatFuture: Future[Boolean] = heatWater(Water(25)) flatMap {
      water => temperatureOkay(water)
    }

    val acceptable: Future[Boolean] = for {
      heatedWater <- heatWater(Water(25))
      okay <- temperatureOkay(heatedWater)
    } yield okay


    //
    // This is not used concurrently. Only grind success then heatWater start
    //

    def prepareCappuccinoSequentially(): Future[Cappuccino] =
      for {
        ground <- grind("arabica beans")
        water <- heatWater(Water(25))
        foam <- frothMilk("milk")
        espresso <- brew(ground, water)
      } yield combine(espresso, foam)


    //
    // if you want grid, heatWater, frothMilk execute concurrent.
    // You must instantiate Future in advanced
    // The following example show that brew always execute in the last.
    // But grind, heatWater , frontMilk will execute concurrently.
    //

    def prepareCappuccino(): Future[Cappuccino] = {
      val groundCoffee = grind("arabica beans")
      val heatedWater = heatWater(Water(20))
      val frothedMilk = frothMilk("milk")
      for {
        ground <- groundCoffee
        water <- heatedWater
        foam <- frothedMilk
        espresso <- brew(ground, water)
      } yield combine(espresso, foam)
    }

    Thread.sleep(1000)
  }

  def temperatureOkay(water: Water): Future[Boolean] = Future {
    (80 to 85) contains (water.temperature)
  }

  def prepareCappuccino(): Future[Cappuccino] = {
    val groundCoffee = grind("arabica beans")
    val heatedWater = heatWater(Water(20))
    val frothedMilk = frothMilk("milk")
    for {
      ground <- groundCoffee
      water <- heatedWater
      foam <- frothedMilk
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }



  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding...")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "baked beans") throw GrindingException("are you joking?")
    println("finished grinding...")
    s"ground coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("heating the water now")
    Thread.sleep(Random.nextInt(2000))
    println("hot, it's hot!")
    water.copy(temperature = 85)
  }



  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("milk frothing system engaged!")
    Thread.sleep(Random.nextInt(2000))
    println("shutting down milk frothing system")
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("happy brewing :)")
    Thread.sleep(Random.nextInt(2000))
    println("it's brewed!")
    "espresso"
  }

  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

}
