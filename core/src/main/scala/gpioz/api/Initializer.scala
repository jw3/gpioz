package gpioz.api

import gpioz.Gpioz.{GpInitRes, GpioInitializer}
import org.bytedeco.javacpp.pigpio
import zio.IO

/**
  *
  */
trait Initializer {

  /**
    * Initialises the library.  Call before using the other library functions.
    * Returns the pigpio version number if OK, otherwise PI_INIT_FAILED.
    */
  def gpioInitialise()(implicit init: GpioInitializer): GpInitRes = init

  /**
    * Terminates the library.  Call before program exit.
    * Resets the used DMA channels, releases memory, and terminates any running threads.
    */
  //def gpioTerminate(): IO[Nothing, Unit]
}

object DefaultInitializer extends DefaultInitializer

trait DefaultInitializer extends Initializer {
  implicit def gpioInitialiser: GpioInitializer = IO.succeed(pigpio.gpioInitialise()).flatMap(InitResult(_))

  implicit def gpioTerminate(): IO[Nothing, Unit] =
    IO.succeed(pigpio.gpioTerminate())
}
