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

    libraryDependencies += "stirling " %% "stirling" % "1.1.1"


Hacking
-------

Open a FIX console:

    ./sbt one-jar
    java -jar stirling.jar

Run all unit tests:

    ./sbt test

Run the performance test:

    ./sbt make-perftest
    script/perftest
