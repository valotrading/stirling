# Stirling

[![Build Status](https://travis-ci.org/valotrading/stirling.png?branch=master)](https://travis-ci.org/valotrading/stirling)

Stirling is an open source electronic trading library for the JVM.


## Features

  - FIX Engine with the following profiles:
    - BATS Europe
    - Burgundy
    - Chi-X Europe
    - Hotspot FX
    - Lime Brokerage
    - MB Trading
    - NASDAQ OMX
    - UBS
  - ITCH with the following profiles:
    - BATS TCP PITCH 1.12.0
    - BATS TOP 1.2.0
    - NASDAQ OMX Nordic Equity TotalView-ITCH 1.86
    - NASDAQ TotalView-ITCH 4.1
  - MB Trading Quote API


## Installation

Stirling is available at a Maven repository. Add this into your `build.sbt`:

    resolvers += "stirling-repository" at "http://valotrading.github.com/stirling/maven"

    libraryDependencies += "stirling" %% "stirling" % "1.3.1"


## License

Stirling is released under the Apache License, Version 2.0.
