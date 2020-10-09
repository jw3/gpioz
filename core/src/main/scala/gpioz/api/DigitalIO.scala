package gpioz.api

import gpioz.api2.UserGpio

trait DigitalIO {
  def gpioGetMode(gpio: UserGpio): Int
  def gpioSetMode(gpio: UserGpio, mode: PinMode): Unit
  def gpioRead(gpio: UserGpio): Int
  def gpioWrite(gpio: UserGpio, level: Level): Int
  def gpioSetPullUpDown(gpio: UserGpio, pull: GpioPull): Int
//  def watch(gpio: UserGpio, q: Queue[GpioAlert]): Int
}
