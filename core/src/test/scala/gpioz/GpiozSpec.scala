package gpioz

import gpioz.Gpioz.{GpIO, GpInitRes}
import gpioz.api._
import org.scalatest.{Matchers, WordSpec}
import zio.IO

object Fakez {
  implicit def fakeInitz(): GpInitRes = InitResult(100)

  implicit def fakeReader(p: UserGpio): GpIO[Level] = IO.succeed(High)

  implicit def fakeWriter(p: UserGpio, l: Level): GpIO[GpioResult] = IO.succeed(GpioOk)
}

class GpiozSpec extends WordSpec with Matchers with Initializer with DigitalIO {
  val rt = zio.Runtime.default

  import Fakez._

  "fake gpioz" should {
    "init" in {
      rt.unsafeRun(
        for {
          res ← gpioInitialise()(Fakez.fakeInitz)
        } yield res shouldBe Init(100)
      )
    }
    "read" in {
      val expected = High
      rt.unsafeRun(
        for {
          level ← gpioRead(UserGpio(4))
        } yield level shouldBe expected
      )
    }
    "write" in {
      rt.unsafeRun(
        for {
          res ← gpioWrite(UserGpio(4), High)
        } yield res shouldBe GpioOk
      )
    }
  }
}
