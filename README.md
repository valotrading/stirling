xTCH encoder/decoder
====================

xTCH is a library for encoding and decoding protocols similar to the NASDAQ
OMX's INET low-latency market data (ITCH) and order entry (OUCH) protocols.
xTCH also introduces session management for the supported protocol profiles.

Protocol Profiles
-----------------

Currently, xTCH supports the following profiles:

- Soup TCP 2.0 (subset)
- Turquoise Native API
- ITCH 1.86

References
----------

1. [Nasdaq OMX Trading Specifications] [1]
   [1]: http://www.nasdaqtrader.com/Trader.aspx?id=TradingSpecs
2. [Turquoise Techical Specification (TQ301), Issue 1.4] [2]
   [2]: http://www.tradeturquoise.com/doclibrary/TQ301_Trading_Gateway_NATIVE.pdf
3. [Nordic Equity TotalView-ITCH 1.86] [3]
   [3]: http://nordic.nasdaqomxtrader.com/digitalAssets/72/72740_nordic_equity_totalview-itch_1.86.pdf
