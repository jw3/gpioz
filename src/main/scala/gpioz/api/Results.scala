package gpioz.api

import org.bytedeco.javacpp.pigpio

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

sealed trait GpioResult
sealed trait GpioFailure extends RuntimeException with GpioResult

sealed trait GpioOk extends GpioResult
case class OK() extends GpioOk

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
  def apply(code: Int): GpioResult = code match {
    case 0                       => OK()
    case pigpio.PI_BAD_USER_GPIO => throw BadUserGpio()
    case pigpio.PI_BAD_GPIO      => throw BadExGpio()
    case pigpio.PI_BAD_MODE      => throw BadMode()
    case pigpio.PI_BAD_PUD       => throw BadPull()
    case _                       => throw UnknownFailure()
  }
}

object GpioResultOf {
  def apply(f: => Int): Try[GpioResult] = {
    try Success(GpioResult(f))
    catch {
      case NonFatal(e) => Failure(e)
    }
  }
}
