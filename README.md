# clj-soap-srv

Here is short example of implementation SOAP server on clojure on top of JAX-WS.2.0.

## Installation

- Install Java
- Install Leiningen https://leiningen.org/
- Clone repo
- Copy paste WSDL files to `./resources/wsdl`
- Generate java classes by calling:

		$ make wsimport

## Playing with it

To run REPL just call:

	$ lein repl

Then enter in repl next commands:

	$ (ns dev)
	$ (reload-system!)

We just started endpoint implementing our service, after any change we've made in code - system can be restarted and reload application's code.
To check that all system working properly:

- Check generated wsdl file: http://localhost:8080/ConcurHotelServiceWS?wsdl
- Run example request to system in new terminal window:

		$ make ping

To see how changing code works, go to `clj-soap-srv/api.clj` and slightly change `get-hotel-name` function. Then return back to terminal #1, and call:

	$ (reload-system!)

Calling `$ make example` after that should return new response.

## I want just run it

Here is easiest way to run app with leiningen:

	$ lein run

Then from new terminal window call:

	$ make ping

You should see response from soap server. You can also check generated wsdl, xsd file at:

- Check generated xsd file: http://localhost:8080/HotelInfoWS?xsd=1
