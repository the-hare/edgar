# edgar

A basic tool for performing technical Analysis on financial instruments. So far, the emphasis will be on
* stock market trading
* algorthmic, automated trading
* using the Interactive Brokers (www.interactivebrokers.com) trading platform

This tool uses the [Pedestal](http://pedestal.io/) Webapplication framework. Http service code will be at ***src/edgar/service.clj*** (with tests at ***test/edgar/service_test.clj***). To configure logging see config/logback.xml. By default, the app logs to stdout and logs/. To learn more about configuring Logback, read its [documentation](http://logback.qos.ch/documentation.html). And [other examples](https://github.com/pedestal/samples).

## Prerequisites

You will need these tools installed on your machine.

1. [Leiningen](https://github.com/technomancy/leiningen), 2.0 or abouve
2. [Datomic](http://www.datomic.com)
3. Interactive Brokers [API Gateway](https://www.interactivebrokers.com/en/?f=%2Fen%2Fsoftware%2Fibapi.php)

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 FIXME
