package gpioz.api

import org.bytedeco.javacpp.pigpio
import zio.IO

sealed class GpioFailure extends RuntimeException
sealed trait InitFailure extends GpioFailure
case object InitFailed extends InitFailure

sealed trait GpioResult
case object GpioOk extends GpioResult

sealed trait InitResult
case class Init private[api] (ver: Int) extends InitResult

object InitResult {
  def apply(code: Int): IO[InitFailure, InitResult] = code match {
    case pigpio.PI_INIT_FAILED => IO.fail(InitFailed)
    case ver: Int              => IO.succeed(Init(ver))
  }
}

/**
  * [[Gpio]] failures
  */
sealed trait BadGpio extends GpioFailure
case class BadUserGpio() extends BadGpio
case class BadExGpio() extends BadGpio

case class BadMode() extends GpioFailure
case class BadPull() extends GpioFailure
case class BadLevel() extends GpioFailure
case class UnknownFailure() extends GpioFailure

object GpioResult {
  def apply(code: Int): Either[GpioFailure, GpioResult] = code match {
    case 0                       => Right(GpioOk)
    case pigpio.PI_BAD_USER_GPIO => Left(BadUserGpio())
    case pigpio.PI_BAD_GPIO      => Left(BadExGpio())
    case pigpio.PI_BAD_MODE      => Left(BadMode())
    case pigpio.PI_BAD_PUD       => Left(BadPull())
    case _                       => Left(UnknownFailure())
  }
}
