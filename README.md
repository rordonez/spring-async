# spring-async
The project has been developed using Spring Boot, Spring Hateoas and Spring Data. The objective of this project is to show the Async feature add in the specification Servlet 3.0.

The API only has a Controller with a POST method. The resource's name is /operation. To make a new operation, a new POST request is required using the following Json:

{
	"id": "3",
	"firstOperand": 1,
	"secondOperand" : 2
}

All these fields are required. The request method returns data in Json format and the return type is a Callable object. That means the data is not returned inmediately, but in 2 seconds.
There is also defined a Cache for third party operations that could consume a lot of time.

To handle the errors, it exists a ControllerAdvice class that returns a Json Object with an error description. This mechanism is very basic, but it could be improved adding another exception handlers and adding new fields to the response object for errors.

OBTAINING DATA

curl -H "Content-Type: application/json" -X POST -d '{"id":"xyz","firstOperand":1, "secondOperand":2}' http://localhost:8080/operation
