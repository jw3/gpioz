package gpioz.api

import org.bytedeco.javacpp.pigpio

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

/**
  *
  */
trait Initializer {

  /**
    * Initialises the library.  Call before using the other library functions.
    * Returns the pigpio version number if OK, otherwise PI_INIT_FAILED.
    */
  def gpioInitialise(): Try[InitResult]

  /**
    * Terminates the library.  Call before program exit.
    * Resets the used DMA channels, releases memory, and terminates any running threads.
    */
  def gpioTerminate(): Unit
}

object DefaultInitializer extends Initializer {
  def gpioInitialise(): Try[Init] = {
    try Success(InitResult(pigpio.gpioInitialise()))
    catch {
      case NonFatal(e) => Failure(e)
    }
  }
  def gpioTerminate(): Unit = pigpio.gpioTerminate()
}
