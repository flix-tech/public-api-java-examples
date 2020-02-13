This is the Java example code FlixMobility provides for the Public API.
Please refer to the online documentation we provide on our 
[Developer Portal](https://developer.flix.tech/).

# Dependencies
- Java 11
- Maven

# Quick start
1. Replace the values of INTEGRATION_EMAIL and INTEGRATION_TOKEN in 
/src/main/java/com.flixbus.api.util.ExampleCodeHelper with your credentials.
2. Run the main method in /src/main/java/com.flixbus.api.SearchExample.

# How to use the examples
The examples reflect use cases like Search and Booking which includes 
necessary steps like authentication or getting passenger details. So if you 
are specifically looking for an example on how to accomplish authentication
for instance you can find this in the code of those examples.

We provide the example code in order to reduce your effort during the 
implementation of your client. You might want to consider to use the classes 
in package com.flixbus.api.domain as is since the deserialization from api 
responses works out of the box (using the Jackson library).

However we do not make any warranties about the completeness of the code.
You will need to implement you client due to your business requirements.