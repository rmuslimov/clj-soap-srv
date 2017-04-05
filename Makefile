.PHONY: example wsimport

example:
	curl -s --header "Content-Type: text/xml;charset=UTF-8" --data @curl-client/data.xml http://localhost:8080/HotelInfoWS | xmllint --format -

javac:
	lein with-profile base,system javac

wsimport:
	wsimport -target 2.0 -s ./src/java ./resources/wsdl/HotelService2.wsdl -XadditionalHeaders -XdisableAuthenticator -Xdebug
