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


#### FIX Protocol Profiles

In order to support messages of various FIX protocol profiles, Stirling allows
definition of message classes that are profile specific. In most cases, a
profile specific FIX message comprises of fields, which can be categorized by
their value as follows: (a) the values of a field is fully specified by the
standard, (b) the values of a field is a subset of the standard, or (c) the
values of a field is not defined in the standard and may assign another meaning
for a value that is specified in the standard.

If the field is defined as it is defined in the standard, then the standard
field type is used. The fields following the standard are placed under the
`stirling.fix.tags` package.

A profile specific enumeration is introduced when the standard defines a string
field and the profile specifies an enumeration of possible values. A profile
specific enumeration is also introduced when the profile uses non-standard
values in the enumeration. However, if the profile uses a subset of an
enumeration, Stirling uses the standard field type.

In cases where a field is a combination of multiple characters, a field is
based on the string field type and the validation of such fields is left for
the user.


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
