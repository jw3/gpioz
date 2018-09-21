package gpioz.api

import org.bytedeco.javacpp.pigpio
import scalaz.zio.IO


sealed trait GpioFailure extends RuntimeException

sealed trait GpioResult
case object GpioOk extends GpioResult

sealed trait InitResult
case class Init private[api] (ver: Int) extends InitResult
case object InitFailed extends RuntimeException with InitResult
case object UnknownInitFailure extends RuntimeException with InitResult

object InitResult {
  def apply(code: Int) = code match {
    case pigpio.PI_INIT_FAILED => throw InitFailed
    case ver: Int              => Init(ver)
  }
}

/**
  * [[Gpio]] failures
  */
case class UnknownFailure() extends GpioFailure

sealed trait BadGpio extends GpioFailure
case class BadUserGpio() extends BadGpio
case class BadExGpio() extends BadGpio

case class BadMode() extends GpioFailure
case class BadPull() extends GpioFailure
case class BadLevel() extends GpioFailure

object GpioResult {
  def apply(code: Int): IO[GpioFailure, GpioResult] = code match {
    case 0                       => IO.point(GpioOk)
    case pigpio.PI_BAD_USER_GPIO => throw BadUserGpio()
    case pigpio.PI_BAD_GPIO      => throw BadExGpio()
    case pigpio.PI_BAD_MODE      => throw BadMode()
    case pigpio.PI_BAD_PUD       => throw BadPull()
    case _                       => throw UnknownFailure()
  }
}
