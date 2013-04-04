# Stirling

[![Build Status](https://travis-ci.org/valotrading/stirling.png?branch=master)](https://travis-ci.org/valotrading/stirling)

Stirling is an open source electronic trading library for the JVM.


## Protocols

  - BATS TCP PITCH 1.12.0, 1.12.2
  - BATS TOP 1.2.0
  - BATS Europe FIX
  - Burgundy FIX
  - Chi-X Europe FIX
  - Hotspot FX FIX
  - Lime Brokerage FIX 3.6.3
  - MB Trading FIX
  - MB Trading Quote API
  - NASDAQ TotalView-ITCH 4.1
  - NASDAQ OMX FIX
  - NASDAQ OMX Nordic Equity TotalView-ITCH 1.86
  - NASDAQ OMX OUCH 2.01
  - Oslo Børs Millennium Exchange ITCH 2.0
  - UBS FIX


## Installation

Stirling is available at a Maven repository. Add this into your `build.sbt`:

    resolvers += "valotrading" at "http://valotrading.github.com/maven"

    libraryDependencies += "stirling" %% "stirling-core" % "1.6.0"


## License

Stirling is released under the Apache License, Version 2.0.
