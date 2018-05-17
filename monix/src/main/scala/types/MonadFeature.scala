package types

object MonadFeature extends App {


  trait ApplicativeType[F[_]] {

    def pure[A](a: A): F[A]

    def product[A, B](a: F[A], b: F[B]): F[(A, B)]

    def map[A, B](a: F[A])(f: A => B): F[B]

    def flatMap[A, B](a: F[A])(f: A => F[B]): F[B]

  }

  private implicit val applicativeOption = new ApplicativeType[Option] {

    override def pure[A](a: A): Option[A] = Some(a)

    override def product[A, B](a: Option[A], b: Option[B]): Option[(A, B)] = {
      a.flatMap(value => b.map(value1 => {
        val tuple = (value, value1)
        tuple
      }))
    }

    override def map[A, B](input: Option[A])(f: A => B): Option[B] = {
      input match {
        case Some(a) => Some(f(a))
        case None => None
      }
    }

    override def flatMap[A, B](input: Option[A])(f: A => Option[B]): Option[B] = {
      input.flatMap(value => f(value))
    }
  }

  val optionIntValue = applicativeOption.pure(10)
  private val maybeInt: Option[Int] = applicativeOption.map(optionIntValue)(a => a * 100)
  println(maybeInt)

  private val maybeFlatMapInt: Option[Int] = applicativeOption.flatMap(optionIntValue)(a => applicativeOption.pure(a * 2000))
  println(maybeFlatMapInt)

  val optionStringValue = applicativeOption.pure("hello")
  private val maybeString: Option[String] = applicativeOption.map(optionStringValue)(a => s"$a applicative world")
  println(maybeString)

  val optionStringValue1 = applicativeOption.pure("hello")
  val optionStringValue2 = applicativeOption.pure(" world")
  private val maybeTuple: Option[(String, String)] = applicativeOption.product(optionStringValue1, optionStringValue2)
  println(maybeTuple)


  //  println(runOption(Some(10), a => a))



}