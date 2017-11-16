package com.scala.future.demo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by Honda on 2017/11/16.
  */
object FutueFallbackTo {
  def main(args: Array[String]): Unit = {
    val usdQuota = Future {
      getCurrentvalue("USD")
      throwException
    }.map { cur => "Value:" + cur }

    val jpnQuota = Future {
      getCurrentvalue("jpn")
    }.map { cur => "Value:" + cur }


    //
    // if usdQuota failed, it will print jpn value
    //

    val anyQuota = usdQuota.fallbackTo( jpnQuota)
    anyQuota.onSuccess{
      case v => println(v)
    }

    Thread.sleep(1000)
  }



  def throwException(): Unit =
  {
    throw new RuntimeException("Exception")
  }
  def getCurrentvalue(currency: String): String = {
    println(currency + ":Get Current Value")
    currency
  }
}
