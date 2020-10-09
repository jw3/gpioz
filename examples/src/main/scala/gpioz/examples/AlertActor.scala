package gpioz.examples

import gpioz.alerts.{Alerts, GpioAlert}
import gpioz.api2.UserGpio
import gpioz.pi.Pi
import zio._


object main extends scala.App {
  val deps = Pi.make() >+> Alerts.live() >+> clock.Clock.live

  val app = for {
    q <- Queue.unbounded[GpioAlert]
    _ <- Alerts.watch(UserGpio(4), q)
    h = for {
      a <- q.take
      _ <- IO.succeed {println(s"alert ${a.tick}")}
    } yield ()
    _ <- h.repeat(Schedule.forever)
  } yield ()

  Runtime.unsafeFromLayer(deps).unsafeRun(app)
}
