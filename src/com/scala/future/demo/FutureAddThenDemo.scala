package com.scala.future.demo

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
import scala.concurrent.ExecutionContext.Implicits.global


/**
  * Created by Honda on 2017/11/16.
  */
object FutureAddThenDemo {

  def main(args: Array[String]): Unit = {
    val allPost = scala.collection.mutable.Set[String]()


    //andThen() returns a copy of the original Future,
    // it does not change the returned Future in anyway.
    // it is used to execute side effects only.
    
    val f = Future {
      val s = getNewPosts()
      println(s)
      s
    }.andThen {
      case Success(post) => {
        println("add post")
        val newP = getNewPosts()
        allPost.+=(post)
        allPost.+=(newP)
        allPost.foreach(println)
        println("==================")
        throwException()
      }
      case Failure(e) => {
        println("fail 1:" + e.getMessage)
      }
    }.andThen {
      case Success(msg) => {
        println("success")
        println(msg.getClass)
        println(msg)
      }
      case Failure(e) => {
        println("fails")
        println(e.getClass)
        println(e.getMessage)
      }

    }

    //      .recover{
    //      case e:Exception =>
    //        {
    //          println("recover")
    //          "handle Exception"
    //        }
    //    }

    f.onComplete {

      case Success(e) => println("Success:" + e)
      case Failure(e) => println("Failure:" + e.getMessage)
    }
    Thread.sleep(2000)

  }

  def getNewPosts(): String = {
    val r = Random.nextInt(100)
    "Post:" + r
  }

  def throwException(): Unit = {
    throw new RuntimeException("runtime Exception")
  }
}
