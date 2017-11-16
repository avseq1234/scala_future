package com.scala.future.demo

import scala.concurrent.{future,promise}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Honda on 2017/11/16.
  */
object Promise {
  def main(args: Array[String]): Unit = {
    val p = promise[String]
    val f = p.future

    val producer = future{
      val r = produceSomething
      println(r)
      p.success(r)
//      p.failure( new RuntimeException("ttt"))
      continueDoSomething

    }

    val consumer = future{
      startDoSomething
      f.onSuccess
      {
        case x => println("in consumer:" + x)
      }

      f.onFailure
      {
        case e => println(e.getMessage)
      }
    }

    Thread.sleep(1000)

  }

  def startDoSomething : String =
  {
    "Start do something"
  }
  def produceSomething : String =
  {
    "produce something"
  }

  def continueDoSomething =
  {
    println("Continue do something")
  }
}
