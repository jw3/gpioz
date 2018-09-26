package gpioz

import gpioz.Gpioz.{GpIO, GpInitRes}
import gpioz.api._
import org.scalatest.{Matchers, WordSpecLike}
import scalaz.zio.{IO, RTS}

object Fakez {
  implicit def fakeInitz(): GpInitRes = {
    for {
      _ ← IO.sync(println("fake init"))
      res ← InitResult(100)
    } yield res
  }

  implicit def fakeReader(p: UserGpio): GpIO[Level] = {
    for {
      _ ← IO.sync(println("fake reading"))
      res ← IO.now(High)
    } yield res
  }

  implicit def fakeWriter(p: UserGpio, l: Level): GpIO[GpioResult] = {
    for {
      _ ← IO.sync(println("fake writing"))
      res ← IO.now(GpioOk)
    } yield res
  }
}

class GpiozSpec extends RTS with WordSpecLike with Matchers with Initializer with DigitalIO {

  import Fakez._

  "fake gpioz" should {
    "init" in {
      unsafeRun(
        for {
          res ← gpioInitialise()(Fakez.fakeInitz)
        } yield res shouldBe Init(100)
      )
    }
    "read" in {
      val expected = High
      unsafeRun(
        for {
          level ← gpioRead(UserGpio(4))
        } yield level shouldBe expected
      )
    }
    "write" in {
      unsafeRun(
        for {
          res ← gpioWrite(UserGpio(4), High)
        } yield res shouldBe GpioOk
      )
    }
  }
}
