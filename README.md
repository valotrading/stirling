Stirling
========

Stirling is an open source electronic trading library for the JVM.


Features
--------

  - FIX Engine with the following profiles:
    - BATS Europe
    - Burgundy
    - Chi-X Europe
    - Hotspot FX
    - MB Trading
    - UBS
  - ITCH with the following profiles:
    - NASDAQ OMX Nordic Equity TotalView-ITCH 1.86
  - MB Trading Quote API


Installation
------------

Stirling is available at a Maven repository. Add this into your `build.sbt`:

    resolvers += "stirling-repository" at "http://valotrading.github.com/stirling/maven"

    libraryDependencies += "stirling " %% "stirling" % "1.1.3"


License
-------

Stirling is released under the Apache License, Version 2.0.
