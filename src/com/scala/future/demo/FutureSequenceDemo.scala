package com.scala.future.demo


import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Created by Honda on 2017/11/10.
  */
object FutureSequenceDemo {

  def computation(): Int = { 25 + 50 }


  def main(args: Array[String]): Unit = {
    val theFuture = Future { computation() }
    theFuture.onComplete {
      case Success(result) => println(result)
      case Failure(t) => println(s"Error: %{t.getMessage}")
    }

    val f1 = Future {
      TimeUnit.SECONDS.sleep(1)
      "f1"
    }
    val f2 = Future {
      TimeUnit.SECONDS.sleep(2)
      "f2"
    }
    val f3 = Future {
      TimeUnit.SECONDS.sleep(3)
      2342
    }

        val f5: Future[(String, String, Int)] =
          for {
            r2 <- f2
            r3 <- f3
            r1 <- f1
          } yield (r1, r2, r3)

    //
    // f5 is identical with f4
    //
    val f4 = Future.sequence(Seq(f1, f2, f3))
    val results= Await.result(f4, 4.seconds)
      val result1s= Await.result(f5, 4.seconds)
    println(results)
    println(result1s)
  }
}
