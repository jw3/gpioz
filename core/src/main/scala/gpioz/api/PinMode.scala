package gpioz.api

import org.bytedeco.javacpp.pigpio

sealed trait PinMode {
  def value: Int
}

case object ClearPin extends PinMode { val value: Int = pigpio.PI_CLEAR }
case object InputPin extends PinMode { val value: Int = pigpio.PI_INPUT }
case object OutputPin extends PinMode { val value: Int = pigpio.PI_OUTPUT }
case object QueryPinMode

object PinMode {
  def apply(value: Int): PinMode = value match {
    case pigpio.PI_INPUT  => InputPin
    case pigpio.PI_OUTPUT => OutputPin
    case _                => throw BadMode()
  }
}

object PinModes {
  val input: PinMode = InputPin
  val output: PinMode = OutputPin
}
