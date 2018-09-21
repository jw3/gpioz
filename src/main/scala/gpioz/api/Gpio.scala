package gpioz.api

import org.bytedeco.javacpp.pigpio
import org.bytedeco.javacpp.pigpio.gpioAlertFunc_t

/**
  * GPIO
  *
  * A Broadcom numbered gpio, in the range 0-53.
  *
  * There  are 54 General Purpose Input Outputs (gpios) named gpio0 through gpio53.
  *
  * They are split into two  banks.   Bank  1  consists  of  gpio0  through
  * gpio31.  Bank 2 consists of gpio32 through gpio53.
  *
  * All the gpios which are safe for the user to read and write are in
  * bank 1.  Not all gpios in bank 1 are safe though.  Type 1 boards
  * have 17  safe gpios.  Type 2 boards have 21.  Type 3 boards have 26.
  *
  * See [*gpioHardwareRevision*].
  *
  * The user gpios are marked with an X in the following table.
  *
  * 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
  * Type 1    X  X  -  -  X  -  -  X  X  X  X  X  -  -  X  X
  * Type 2    -  -  X  X  X  -  -  X  X  X  X  X  -  -  X  X
  * Type 3          X  X  X  X  X  X  X  X  X  X  X  X  X  X
  * 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
  * Type 1    -  X  X  -  -  X  X  X  X  X  -  -  -  -  -  -
  * Type 2    -  X  X  -  -  -  X  X  X  X  -  X  X  X  X  X
  * Type 3    X  X  X  X  X  X  X  X  X  X  X  X  -  -  -  -
  *
  */
sealed trait Gpio {
  def value: Int
}

case class UserGpio private[api] (value: Int) extends Gpio
case class ExtGpio private[api] (value: Int) extends Gpio

object Gpio {
  val userPins = Range(0, pigpio.PI_MAX_USER_GPIO)
  val extPins = Range(0, pigpio.PI_MAX_GPIO)

  // default behavior of Gpio is user-gpios
  def apply(num: Int): UserGpio = {
    require(userPins.contains(num), "out of range")
    UserGpio(num)
  }

  def ext(num: Int): ExtGpio = {
    require(extPins.contains(num), "out of range")
    ExtGpio(num)
  }
}

object GpioImplicits {
  implicit def int2gpio(int: Int): UserGpio = Gpio(int)
}

sealed trait GpioAlert {
  def gpio: UserGpio
  def level: Level
  def tick: Long
}

object GpioAlert {
  def apply(user_gpio: Int, gpio_level: Int, microtick: Int /*UINT32*/ ) = {
    new GpioAlert {
      lazy val gpio: UserGpio = UserGpio(user_gpio)
      lazy val level: Level = Level.unsafeOf(gpio_level)
      lazy val tick: Long = Integer.toUnsignedLong(microtick)
    }
  }
  def unapply(arg: GpioAlert): Option[(UserGpio, Level, Long)] =
    Option((arg.gpio, arg.level, arg.tick))
}

// todo revisit with scalaz
//object GpioAlertFunc {
//  // pigpio-docs: The alert may be cancelled by passing NULL as the function.
//  val clear: GpioAlertFunc = null.asInstanceOf[GpioAlertFunc]
//}

// todo revisit with scalaz
//class GpioAlertFunc(ref: ActorRef) extends gpioAlertFunc_t {
//  def callback(gpio: Int, level: Int, tick: Int /*UINT32*/ ): Unit = {
//    ref ! GpioAlert(gpio, level, tick)
//  }
//}
