package com.scala.future.demo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
/**
  * Created by Honda on 2017/11/10.
  */
object FutureRecover {
  def main(args: Array[String]): Unit = {


    val f2 = Future {
      throw new RuntimeException("throw exception")
    }

    f2.onComplete{

      case Success(e) => println(e)
      case Failure(e) => println(e.getMessage)
    }


    val f3 = Future {
      throw new RuntimeException("throw exception")
    }.recover {
      case e: Exception =>
        "handled exception"
    }

    f3.onComplete{

      case Success(e) => println(e)
      case Failure(e) => println(e.getMessage)
    }

    Thread.sleep(1000)


  }

}
