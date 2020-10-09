package gpioz

import gpioz.api._
import org.bytedeco.javacpp.pigpio
import zio._

object pi {
  val userPins = Range(0, pigpio.PI_MAX_USER_GPIO)
  val extPins = Range(0, pigpio.PI_MAX_GPIO)

  type Pi = Has[Pi.Service]
  object Pi {
    trait Service {
      def initialize(): IO[InitFailure, InitResult]
      def terminate(): UIO[Unit]
      def gpio(): UIO[DigitalIO]
    }

    /*

trait DefaultDigitalIO extends DigitalIO {
  implicit def gpioModeReader: ConfigGet[PinMode] =
    (p: UserGpio) => IO.succeed(pigpio.gpioGetMode(p.value)).flatMap(x => PinMode(x))

  implicit def gpioModeSetter: ConfigSet[PinMode] =
    (p: UserGpio, m: PinMode) => IO.succeed(pigpio.gpioSetMode(p.value, m.value)).flatMap(GpioResult(_))

  implicit def gpioReader: PinReader[Level] =
    (p: UserGpio) => IO.succeed(pigpio.gpioRead(p.value)).flatMap(Level(_))

  implicit def gpioWriter: PinWriter[Level] =
    (p: UserGpio, l: Level) => IO.succeed(pigpio.gpioWrite(p.value, l.value)).flatMap(GpioResult(_))

  implicit def gpioPullUpDown: ConfigSet[GpioPull] =
    (p: UserGpio, pull: GpioPull) => IO.succeed(pigpio.gpioWrite(p.value, pull.value)).flatMap(GpioResult(_))

  implicit def setAlertFunc: ConfigSet[GpioAlertFunc] =
    (p: UserGpio, f: GpioAlertFunc) => IO.succeed(pigpio.gpioSetAlertFunc(p.value, f)).flatMap(GpioResult(_))
}

     */

    def make(): ULayer[Pi] = ZLayer.succeed(
      new Service {
        val dio = new DigitalIO {
          def gpioGetMode(gpio: api2.UserGpio): Int = pigpio.gpioGetMode(gpio.value)
          def gpioSetMode(gpio: api2.UserGpio, mode: PinMode): Unit = pigpio.gpioSetMode(gpio.value, mode.value)
          def gpioRead(gpio: api2.UserGpio): Int = pigpio.gpioRead(gpio.value)
          def gpioWrite(gpio: api2.UserGpio, level: Level): Int = pigpio.gpioWrite(gpio.value, level.value)
          def gpioSetPullUpDown(gpio: api2.UserGpio, pull: GpioPull): Int =
            pigpio.gpioSetPullUpDown(gpio.value, pull.value)
        }

        def initialize(): IO[InitFailure, InitResult] = IO.succeed(pigpio.gpioInitialise()).flatMap(InitResult(_))
        def terminate(): UIO[Unit] = IO.succeed(pigpio.gpioTerminate())
        def gpio(): UIO[DigitalIO] = UIO.succeed(dio)
      }
    )

    def initialize(): ZIO[Pi, InitFailure, InitResult] = ZIO.accessM(_.get.initialize())
    def terminate(): Canceler[Pi] = ZIO.accessM(_.get.terminate())
    def gpio(): URIO[Pi, DigitalIO] = ZIO.accessM(_.get.gpio())
  }
}
