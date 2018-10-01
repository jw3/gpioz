gpioz
===

Raspberry Pi gpio control with Scalaz, pigpio, and JavaCPP


### JavaCPP

See https://github.com/jw3/javacpp-pigpio


### docker build

The examples can be built into a docker image.  The image defaults to using `openjdk:8` but can be overridden with environment `BASE_IMAGE`

`BASE_IMAGE=jwiii/pigpio sbt examples/docker:publishLocal`
