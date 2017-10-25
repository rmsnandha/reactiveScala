package app.impl.scalaz

import scala.concurrent.Future

/**
  * Created by pabloperezgarcia on 24/10/2017.
  *
  * Since for comprehension basically flatMap the monads that you pass, in case you need a double
  * flatMap with side effects you can use Monad transformers.
  *
  */
class MonadTransformer {

  def findUserById(id: Long) = Future {
    Some("paul")
  }

  def findAddressByUser(user: String) = Future {
    Some(s"address of $user")
  }


  /**
    * This monad transformer receive a Future o Option in his constructor and implement
    * map to transform the value of the monad, and flatMap to get the value of the option.
    *
    * @param value
    * @tparam A
    */
  case class FutOpt[A](value: Future[Option[A]]) {

    def map[B](f: A => B): FutOpt[B] =
      FutOpt(value.map(optA => optA.map(f)))

    def flatMap[B](f: A => FutOpt[B]): FutOpt[B] =
      FutOpt(value.flatMap(opt => opt match {
        case Some(a) => f(a).value
        case None => Future.successful(None)
      }))
  }

  def findAddressByUserId(id: Long): Future[Option[String]] =
    (for {
      user <- FutOpt(findUserById(id))
      address <- FutOpt(findAddressByUser(user))
    } yield address).value

}
