package com.scala.future.demo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Created by Honda on 2017/11/10.
  */
object FutureBasicDemo {
  def main(args: Array[String]): Unit = {
    val sumFuture = Future[Int] {
      var sum = 0
      for (i <- Range(1, 100000)) sum = sum + i
//      throwException("for failure")
      sum
    }

  //
  // onComplete need a Try[T] => U parameter
  //

  sumFuture.onComplete {
    case Success(result) => println("onComplete success")
    case Failure(e) => println("onComplete:" + e.getMessage)
  }

  sumFuture.onSuccess {
    case sum => println(sum)
  }

  sumFuture.onFailure {
    case result => println("OnFailure:" + result.getMessage)
  }

  Await.result(sumFuture, Duration.Inf)
}

private def throwException (msg: String): Unit = {
  throw new RuntimeException (msg)
}
}
