package com.scala.future.demo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Created by Honda on 2017/11/10.
  */
object FutureMapFlatMapDemo {
  def main(args: Array[String]): Unit = {
    val firstValFuture = Future[Int] {
      1
    }

    firstValFuture.onSuccess
    {
      case msg => println("FirstValue:" + msg)
    }

    val secondFuture = firstValFuture.map { num =>
      println("firstFuture: " + num)
      num + "111"
    }


    secondFuture.onSuccess {
      case result => println(result)
    }


    //
    // If use flatMap, you need return Future
    //

    val thirdFuture = firstValFuture.flatMap { num =>
      println("firstFuture: " + num)
      Future {
        num + "11133333"
      }
    }

    thirdFuture.onSuccess {
      case result => println(result)
    }

    Thread.sleep(1000)
  }
}
