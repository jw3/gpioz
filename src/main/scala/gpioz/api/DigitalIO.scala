package gpioz.api

import gpioz.Gpioz._
import org.bytedeco.javacpp.pigpio
import scalaz.zio.IO

/**
  *
  */
trait DigitalIO {
  def gpioGetMode(gpio: UserGpio)(implicit r: ConfigGet[PinMode]): GpIO[PinMode] = r(gpio)

  def gpioSetMode(gpio: UserGpio, mode: PinMode)(implicit w: ConfigSet[PinMode]): GpIO[GpioResult] = w(gpio, mode)

  def gpioRead(gpio: UserGpio)(implicit r: PinReader[Level]): GpIO[Level] = r(gpio)

  def gpioWrite(gpio: UserGpio, level: Level)(implicit w: PinWriter[Level]): GpIORes = w(gpio, level)

  def gpioSetPullUpDown(gpio: UserGpio, pull: GpioPull)(implicit w: ConfigSet[GpioPull]): GpIORes = w(gpio, pull)
}

object DefaultDigitalIO extends DefaultDigitalIO

trait DefaultDigitalIO extends DigitalIO {
  implicit def gpioModeReader: ConfigGet[PinMode] =
    (p: UserGpio) ⇒ IO.sync(pigpio.gpioGetMode(p.value)).map(PinMode(_))

  implicit def gpioModeSetter: ConfigSet[PinMode] =
    (p: UserGpio, m: PinMode) ⇒ IO.sync(pigpio.gpioSetMode(p.value, m.value)).flatMap(GpioResult(_))

  implicit def gpioReader: PinReader[Level] =
    (p: UserGpio) ⇒ IO.sync(pigpio.gpioRead(p.value)).map(Level(_))

  implicit def gpioWriter: PinWriter[Level] =
    (p: UserGpio, l: Level) ⇒ IO.sync(pigpio.gpioWrite(p.value, l.value)).flatMap(GpioResult(_))

  implicit def gpioPullUpDown: ConfigSet[GpioPull] =
    (p: UserGpio, pull: GpioPull) ⇒ IO.sync(pigpio.gpioWrite(p.value, pull.value)).flatMap(GpioResult(_))
}
