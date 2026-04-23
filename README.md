# SmartCampusCW

OVERVIEW:
The Smart Campus API is a RESTful web service that contains three main resources. Rooms are able to be listed out, created and deleted. Sensors are also able to be listed, created and deleted with the additional functionality of a query if required. Sensor readings are stored in memory maps and users are able to access the reading history of any given sensor or append new readings. 
This API also includes validation and error handling with custom exceptions to ensure that the code works even if problems occur. 

HOW TO BUILD AND RUN:
1.	Ensure you have the correct software required. I personally NetBeans for this along with Glassfish Server.
2.	Open the project in NetBeans
3.	Clean and build the project
4.	Set Glassfish as your run server
5.	Run the project
You can confirm it is running by opening the following URL in your browser
http://localhost:8080/SmartCampusCW/api/v1/rooms

EXAMPLE CURL COMMANDS:

Create a room

curl -X POST http://localhost:8080/SmartCampusCW/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"name":"Lab Alpha","capacity":30}'

Get all rooms

curl http://localhost:8080/SmartCampusCW/api/v1/rooms

Create a sensor

curl -X POST http://localhost:8080/SmartCampusCW/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"type":"CO2","status":"active","currentValue":400.0,"roomId":"your-room-id-here"}'

Get all sensors filtered by type

curl http://localhost:8080/SmartCampusCW/api/v1/sensors?type=CO2

Add a reading to a sensor

curl -X POST http://localhost:8080/SmartCampusCW/api/v1/sensors/your-sensor-id-here/readings \
  -H "Content-Type: application/json" \
  -d '{"value":412.5,"timestamp":"2025-01-01T10:00:00"}'

QUESTIONS:
Part 1
1.	By default, JAX-RS creates a new instance of the resource class for each request made. However, the way this works means that every instance will be discarded when the request is completed, leading to the loss of the data of the instance. The way to circumvent this issue is to write to a hash map and declare the data structures as static fields so that all the requests are written to the same HashMap. This can lead to problems as the threads can attempt to overwrite each other in a singular HashMap which can lead to data corruption so using an alternative to a HashMap like a Concurrent HashMap that is more suited for threads would remove the issue of overwriting as it takes care of synchronisation.
2.	HATEOAS is considered a hallmark as it makes the user experience much more streamlined and easier to navigate as it returns data then also returns follow up links related to the data so that the user can then use that data in a meaningful way. An example of this would be an admin asking for user information then the HATEOAS returning the user information and also a link to update the user info or delete them or change anything regarding their data.
Part 2
1.	Returning either just the IDs or the entire room objects both have their trade-offs. In the case of IDs, the payload is very small which is good for network bandwidth however of the user needs all the information of the room they will then have to make a separate enquiry of the room to get all the useful information. On the other hand, returning the entire room object would contain all the information the user could need but the payload would be much larger which is worse for network bandwidth and could be wasteful if the user only needed some and not all of the room information.
2.	The DELETE operation is idempotent in my implementation as there is no change if the user uses the same command multiple times. On the first time, the room will be deleted and a response will be sent to the user confirming the deletion. On any subsequent times, the room is not there so nothing will change within the server and the server will send back a 404 not found response as there is nothing to change. As there is no difference between each time the command is sent as the final result is the room in question being deleted, the DELETE operation is idempotent.
Part 3
1.	JAX-RS will reject the request and return a 415 Unsupported Media response if it does not match the Json format. This is to prevent complications as the code is only suited for Json format and doesn’t know how to handle anything else like xml. If the request is rejected, the method will never be called to prevent any consequences of any kind.
2.	Query is a generally considered superior as it is a lot more flexible than Path. Path is much more strict as it specifies the exact URL that the user wants which requires more specific instructions whereas Query is a lot easy to implement as adding any extra filters is much simpler and can simply be layered on top of the base URL.  
Part 4
1.	The Sub-Resource Locator Pattern is very well suited for scaling up projects as it allows for growth while keeping everything organised well. If all of the classes that could be separate are instead bundled up into one giant resource class, it becomes a lot more complex to do even the most basic of things like testing code. Instead of being able to easily identify the problem like with the Sub-Resource Locator Pattern, the developer would have to comb through lines and lines of code trying to find one small problem and have to run the entire class every time they wanted to test functionality. Overall, it allows for much better organisation of code for ease of use for the developers.
Part 5
1.	404 response means that whatever the request is trying to reach is simply not able to be found. However the 422 response is more specific and describes the problem in a way that is easier to figure out the issue for as it is used when the server is able to understand the request and find the endpoint but there is a problem with the data that means it cannot be processed properly. This means it is much more semantically accurate as it allows the developer to pinpoint the problem much more accurately.
2.	Exposing Stack Traces is a high security risk as it allows any potential attacker to see the layout of the codebase, which makes it much easier for any attacker to identify potential vulnerabilities in the codebase. A good example is the attacker finding a certain library that is used in the code which happens to have a weakness that is very easily exploitable. 
3.	JAX-RS filters are designed to be able to handle things like logging in the most streamlined and optimised way possible. Rather than manually inserting logging statements which could lead to a variety of problems as they will never be consistent between every method and will log the same code every single time which is highly inefficient, JAX-RS filters handle logging in a much more streamlined way which allows the resource methods to simply run the logic necessary and the filters to be stored in a dedicated filter class that runs purely for the sake of logging. This makes the logging much more consistent as the same class is being used for logging across the board.
