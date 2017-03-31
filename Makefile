.PHONY: example

example:
	curl -s --header "Content-Type: text/xml;charset=UTF-8" --data @curl-client/data.xml http://localhost:8080/HotelInfoWS | xmllint --format -
