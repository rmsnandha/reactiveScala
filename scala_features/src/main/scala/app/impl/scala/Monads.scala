package app.impl.scala


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Monads extends App {

  val f1 = Future {
    Thread.sleep(1000)
    1
  }
  val f2 = Future {
    Thread.sleep(1000)
    2
  }
  val f3 = for {
    v1 <- f1
    v2 <- f2
  } yield v1 + v2

  println(Await.result(f3, 3.second))

  val first = List(1, 2)
  val next = List(8, 9)

  val multiply = first flatMap {
    f => next map {
      n => f * n
    }
  }

  println(multiply)


}
