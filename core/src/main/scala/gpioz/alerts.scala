package gpioz

import gpioz.api.{GpioFailure, GpioResult, Level}
import gpioz.api2.UserGpio
import gpioz.pi.Pi
import org.bytedeco.javacpp.pigpio.gpioAlertFunc_t
import zio._

object alerts {
  sealed trait GpioAlert {
    def gpio: UserGpio
    def level: Level
    def tick: Long
  }

  type Alerts=Has[Alerts.Service]
  object Alerts {
    trait Service {
      def watch(gpio: UserGpio, q: Queue[GpioAlert]): ZIO[Pi, GpioFailure, GpioResult]
    }

    def live(): URLayer[Pi, Alerts] = ZLayer.succeed(
      new Service {
        def watch(gpio: UserGpio, q: Queue[GpioAlert]): ZIO[Pi, GpioFailure, GpioResult] = ???
      }
    )

    def watch(gpio: UserGpio, q: Queue[GpioAlert]): ZIO[Alerts with Pi, GpioFailure, GpioResult] =
      ZIO.accessM(_.get.watch(gpio, q))
  }



  object GpioAlert {
    def apply(user_gpio: Int, gpio_level: Int, microtick: Int /*UINT32*/ ) =
      new GpioAlert {
        lazy val gpio: UserGpio = UserGpio(user_gpio)
        lazy val level: Level = Level.unsafeOf(gpio_level)
        lazy val tick: Long = Integer.toUnsignedLong(microtick)
      }

    def unapply(arg: GpioAlert): Option[(UserGpio, Level, Long)] =
      Option((arg.gpio, arg.level, arg.tick))
  }

  object GpioAlertFunc {
    // pigpio-docs: The alert may be cancelled by passing NULL as the function.
    val clear: gpioAlertFunc_t = null
  }

  class GpioAlertFunc(queue: Queue[GpioAlert]) extends gpioAlertFunc_t {
    def callback(gpio: Int, level: Int, tick: Int /*UINT32*/ ): Unit =
      zio.Runtime.default.unsafeRun(queue.offer(GpioAlert(gpio, level, tick)))
  }
}
