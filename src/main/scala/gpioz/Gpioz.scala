package gpioz

import gpioz.Gpioz.GpIO
import gpioz.api.{GpioResult, _}
import scalaz.zio
import scalaz.zio.IO

object Gpioz {
  type GpIO[R] = IO[GpioFailure, R]

  type PinReader[O] = UserGpio ⇒ GpIO[O]
  type PinWriter[I] = (UserGpio, I) ⇒ GpIO[GpioOk]

  type ConfigGet[O] = PinReader[O]
  type ConfigSet[I] = PinWriter[I]
}

object Fakez {
  implicit def fakeReader(p: UserGpio): GpIO[Level] = {
    for {
      _ ← IO.sync(println("fake reading"))
      res ← IO.now(High)
    } yield res
  }

  implicit def fakeWriter(p: UserGpio, l: Level): GpIO[GpioOk] = {
    for {
      _ ← IO.sync(println("fake writing"))
      res ← GpioResult.apply(0)
    } yield res
  }
}

object Test extends zio.App {
  ExitStatus.ExitNow(1)

  def run(args: List[String]): IO[Nothing, Test.ExitStatus] =
    fn.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))

  import Fakez._

  def fn: IO[GpioFailure, Unit] = {
    val level = High

    for {
      _ ← DefaultDigitalIO.gpioWrite(UserGpio(4), level)
      r2 ← DefaultDigitalIO.gpioRead(UserGpio(5))
    } yield r2 == level
  }
}
