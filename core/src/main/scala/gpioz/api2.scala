package gpioz

object api2 {
  sealed trait Pin {
    def value: Int
  }

  object Pin {
    // default behavior of Gpio is user-gpios
    def apply(num: Int): UserGpio = {
      require(pi.userPins.contains(num), "out of range")
      UserGpio(num)
    }
    def ext(num: Int): ExtGpio = {
      require(pi.extPins.contains(num), "out of range")
      ExtGpio(num)
    }
  }

  case class UserGpio private[api2] (value: Int) extends Pin
  case class ExtGpio private[api2] (value: Int) extends Pin
}
