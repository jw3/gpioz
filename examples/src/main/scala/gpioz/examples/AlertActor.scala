package gpioz.examples

import gpioz.api.{GpioAlert, GpioAlertFunc, UserGpio}
import org.bytedeco.javacpp.pigpio
import zio.{IO, Queue, Schedule}

object AlertActor {
  type Behavior = GpioAlert ⇒ IO[Nothing, Unit]

  def make(p: UserGpio, b: Behavior): IO[Nothing, Unit] = {
    for {
      q ← Queue.unbounded[GpioAlert]
      f = new GpioAlertFunc(q)
      _ = pigpio.gpioSetAlertFunc(p.value, f)
      _ ← q.take.flatMap(b).repeat(Schedule.forever)
    } yield ()
  }
}

object main extends App {
  def printer(a: GpioAlert): IO[Nothing, Unit] =
    for {
      _ ← IO.succeed {println(s"alert ${a.tick}")}
    } yield ()

  def run(args: List[String]): IO[Nothing, Int] =
    for {
      a ← AlertActor.make(UserGpio(4), printer)
    } yield 0
}
