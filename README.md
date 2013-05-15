# edgar

A basic tool for performing technical Analysis on financial instruments. So far, the emphasis will be on
* stock market trading
* algorthmic, automated trading
* using the Interactive Brokers (www.interactivebrokers.com) trading platform

This tool uses the Pedestal Webapplication framework. Http service code will be at ***src/edgar/service.clj*** (with tests at ***test/edgar/service_test.clj***). To configure logging see config/logback.xml. By default, the app logs to stdout and logs/. To learn more about configuring Logback, read its [documentation](http://logback.qos.ch/documentation.html). And [other examples](https://github.com/pedestal/samples).

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 FIXME

# edgar

FIXME

