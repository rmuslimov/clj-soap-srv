.PHONY: ping wsimport

ping:
	curl -s --header "Content-Type: text/xml;charset=UTF-8" --data @curl-client/ping.xml http://localhost:8080/ConcurHotelServiceWS | xmllint --format -

wsimport:
	wsimport -target 2.0 -s ./src/java ./resources/wsdl/HotelService2.wsdl -XadditionalHeaders -XdisableAuthenticator -Xdebug
