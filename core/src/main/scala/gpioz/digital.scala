package gpioz

import gpioz.api._
import gpioz.api2.UserGpio
import gpioz.pi.Pi
import zio._

object digital {
  type DigitalIO = Has[DigitalIO.Service]
  object DigitalIO {

    trait Service {
      def mode(gpio: UserGpio): ZIO[Pi, GpioFailure, PinMode]
      def setMode(gpio: UserGpio, mode: PinMode): ZIO[Pi, GpioFailure, GpioResult]
      def read(gpio: UserGpio): ZIO[Pi, GpioFailure, Level]
      def write(gpio: UserGpio, level: Level): ZIO[Pi, GpioFailure, GpioResult]
      def setPull(gpio: UserGpio, pull: GpioPull): ZIO[Pi, GpioFailure, GpioResult]
    }

    def make(): Layer[Pi, DigitalIO] = ZLayer.succeed(
      new DigitalIO.Service {
        def mode(gpio: UserGpio): ZIO[Pi, GpioFailure, PinMode] = for {
          io <- Pi.gpio()
          r = io.gpioGetMode(gpio)
        } yield PinMode(r)

        def setMode(gpio: UserGpio, mode: PinMode) = ???
        def read(gpio: UserGpio) = ???
        def write(gpio: UserGpio, level: Level) = ???
        def setPull(gpio: UserGpio, pull: GpioPull) = ???
      }
    )

    def gpioGetMode(gpio: UserGpio): ZIO[DigitalIO with Pi, GpioFailure, PinMode] =
      ZIO.accessM(_.get.mode(gpio))

    def gpioSetMode(gpio: UserGpio, mode: PinMode): ZIO[DigitalIO with Pi, GpioFailure, GpioResult] =
      ZIO.accessM(_.get.setMode(gpio, mode))

    def gpioRead(gpio: UserGpio): ZIO[DigitalIO with Pi, GpioFailure, Level] =
      ZIO.accessM(_.get.read(gpio))

    def gpioWrite(gpio: UserGpio, level: Level): ZIO[DigitalIO with Pi, GpioFailure, GpioResult] =
      ZIO.accessM(_.get.write(gpio, level))

    def gpioSetPullUpDown(gpio: UserGpio, pull: GpioPull): ZIO[DigitalIO with Pi, GpioFailure, GpioResult] =
      ZIO.accessM(_.get.setPull(gpio, pull))
  }
}
