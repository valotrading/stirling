Stirling
========

Stirling is an open source electronic trading library for the JVM.


Installation
------------

Stirling is made available through a Maven repository. If you are using SBT,
amend your `build.sbt` with:

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


Components
----------

### FIX Engine

The [FIX Engine][FIX] requires [MongoDB][] 1.4.3 or newer. It uses a database called
`fixengine`. 

[FIX]: http://fixprotocol.org/
[MongoDB]: http://www.mongodb.org/


### ITCH

The ITCH implementation supports the following profiles:

- [NASDAQ OMX Nordic Equity TotalView-ITCH 1.86][ITCH 1.86]

It currently comprises of a message parser. SoupTCP, the transport protocol
used by ITCH, is yet to be implemented.

[ITCH 1.86]: http://nordic.nasdaqomxtrader.com/digitalAssets/72/72740_nordic_equity_totalview-itch_1.86.pdf


### MB Trading Quote API

The MB Trading Quote API implementation supports the market data feed protocol
specified by [MB Trading][].

[MB Trading]: http://www.mbtrading.com/developersMain.aspx?page=api
