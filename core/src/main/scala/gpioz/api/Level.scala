package gpioz.api

import zio.IO

sealed trait Level {
  def value: Int
  def toBoolean: Boolean = value != 0
}

case object High extends Level { val value: Int = 1 }
case object Low extends Level { val value: Int = 0 }

object Level {
  def apply(v: Int): IO[GpioFailure, Level] = IO.succeed(Level.unsafeOf(v))
  def apply(v: Boolean): Level = if (v) High else Low

  def unsafeOf(v: Int): Level = v match {
    case 0 => Low
    case 1 => High
    case _ => throw BadLevel()
  }

  def flip(l: Level): Level = l match {
    case High => Low
    case Low  => High
  }
}

object LevelImplicits {
  implicit def bool2level(b: Boolean): Level = Level(b)
}

object Levels {
  val high: Level = High
  val on: Level = high

  val low: Level = Low
  val off: Level = low

  val set: Level = on
  val clear: Level = off
}
