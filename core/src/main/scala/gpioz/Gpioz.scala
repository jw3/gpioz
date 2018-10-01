package gpioz

import gpioz.api.{GpioResult, _}
import scalaz.zio.IO

object Gpioz {
  type GpIO[R] = IO[GpioFailure, R]
  type GpIORes = GpIO[GpioResult]
  type GpInitRes = GpIO[InitResult]

  type GpioInitializer = () ⇒ GpInitRes
  type PinReader[O] = UserGpio ⇒ GpIO[O]
  type PinWriter[I] = (UserGpio, I) ⇒ GpIORes

  type ConfigGet[O] = PinReader[O]
  type ConfigSet[I] = PinWriter[I]
}
