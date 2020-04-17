package gpioz.api

import org.bytedeco.javacpp.pigpio
import zio.IO

sealed trait GpioPull {
  def value: Int
}

case object PullUp extends GpioPull { val value: Int = pigpio.PI_PUD_UP }
case object PullDown extends GpioPull { val value: Int = pigpio.PI_PUD_DOWN }
case object DontPull extends GpioPull { val value: Int = pigpio.PI_PUD_OFF }

object GpioPull {
  def apply(value: Int): IO[GpioFailure, GpioPull] = value match {
    case pigpio.PI_PUD_UP ⇒ IO.succeed(PullUp)
    case pigpio.PI_PUD_DOWN ⇒ IO.succeed(PullDown)
    case pigpio.PI_PUD_OFF ⇒ IO.succeed(DontPull)
    case _ ⇒ throw BadPull()
  }
}
