package gpioz.api

import org.bytedeco.javacpp.pigpio

sealed trait GpioPull {
  def value: Int
}

case object PullUp extends GpioPull { val value = pigpio.PI_PUD_UP }
case object PullDown extends GpioPull { val value = pigpio.PI_PUD_DOWN }
case object DontPull extends GpioPull { val value = pigpio.PI_PUD_OFF }

object GpioPull {
  def apply(value: Int) = value match {
    case pigpio.PI_PUD_UP   => PullUp
    case pigpio.PI_PUD_DOWN => PullDown
    case pigpio.PI_PUD_OFF  => DontPull
    case _                  => throw BadPull()
  }
}
