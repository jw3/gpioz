package gpioz.api

import org.bytedeco.javacpp.pigpio

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

/**
 *
 */
trait DigitalIO {
  def gpioGetMode(gpio: Gpio): Try[PinMode]
  def gpioSetMode(gpio: Gpio, mode: PinMode): Try[GpioResult]

  def gpioRead(gpio: Gpio): Try[Level]
  def gpioWrite(gpio: Gpio, level: Level): Try[GpioResult]

  def gpioSetPullUpDown(gpio: Gpio, pud: GpioPull): Try[GpioResult]
}

object DefaultDigitalIO extends DefaultDigitalIO

trait DefaultDigitalIO extends DigitalIO {
  def gpioGetMode(gpio: Gpio): Try[PinMode] = {
    try Success(PinMode(pigpio.gpioGetMode(gpio.value)))
    catch {
      case NonFatal(e) => Failure(e)
    }
  }
  def gpioSetMode(gpio: Gpio, mode: PinMode): Try[GpioResult] =
    GpioResultOf(pigpio.gpioSetMode(gpio.value, mode.value))

  def gpioSetPullUpDown(gpio: Gpio, pud: GpioPull): Try[GpioResult] =
    GpioResultOf(pigpio.gpioSetPullUpDown(gpio.value, pud.value))

  def gpioRead(gpio: Gpio): Try[Level] = {
    try Success(Level(pigpio.gpioRead(gpio.value)))
    catch {
      case NonFatal(e) => Failure(e)
    }
  }
  def gpioWrite(gpio: Gpio, level: Level): Try[GpioResult] =
    GpioResultOf(pigpio.gpioWrite(gpio.value, level.value))
}
