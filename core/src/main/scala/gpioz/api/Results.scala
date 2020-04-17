package gpioz.api

import org.bytedeco.javacpp.pigpio
import zio.IO

sealed trait GpioFailure extends RuntimeException
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
  def apply(code: Int): IO[GpioFailure, GpioResult] = code match {
    case 0                       => IO.succeed(GpioOk)
    case pigpio.PI_BAD_USER_GPIO => IO.fail(BadUserGpio())
    case pigpio.PI_BAD_GPIO      => IO.fail(BadExGpio())
    case pigpio.PI_BAD_MODE      => IO.fail(BadMode())
    case pigpio.PI_BAD_PUD       => IO.fail(BadPull())
    case _                       => IO.fail(UnknownFailure())
  }
}
