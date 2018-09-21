package gpioz.api

import org.bytedeco.javacpp.pigpio
import scalaz.zio.IO

/**
  *
  */
trait Initializer {

  /**
    * Initialises the library.  Call before using the other library functions.
    * Returns the pigpio version number if OK, otherwise PI_INIT_FAILED.
    */
  def gpioInitialise(): IO[InitFailure, Int]

  /**
    * Terminates the library.  Call before program exit.
    * Resets the used DMA channels, releases memory, and terminates any running threads.
    */
  def gpioTerminate(): IO[Nothing, Unit]
}

object DefaultInitializer extends Initializer {
  def gpioInitialise(): IO[InitFailure, Int] =
    IO.sync(pigpio.gpioInitialise()).flatMap(InitResult(_))

  def gpioTerminate(): IO[Nothing, Unit] =
    IO.sync(pigpio.gpioTerminate())
}
