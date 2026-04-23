# Smart Campus API
Smart Campus API - Created for Client Server Architecture Coursework 


2. Coursework Tasks - Questions and Answers


------------------------------------
Part 1: Service Architecture & Setup
------------------------------------
Q1 :  In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

Answer : 
Jax-RS creates a new instance of each resource class for every incoming HTTP request. This is known as the per request lifecycle. This means instance variables inside a resource class cannot be used to store shared data. Because each request gets Its own fresh object and any data stored in it would be lost immediately after the response is sent. 

The architectural decision will establish the method for handling in-memory data management. The system generates new resource class instances to handle each incoming request. The system cannot utilize shared data which includes room and sensor lists as part of its instance variables. The class-level static data structure maintains "ConcurrentHashMap" which remains active throughout the server runtime. "ConcurrentHashMap" serves as the chosen data structure because it enables multiple threads to access data concurrently which standard "HashMap" does not provide. The system allows multiple users to access data during simultaneous reading and writing processes while maintaining data integrity and preventing race conditions. The system ensures that two concurrent requests will not disrupt the common data which includes the room addition request and the room retrieval request.

Q2 : Why is the provision of ”Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

Answer:
HATEOAS stands for Hypermedia as the Engine of Application State. The advanced RESTful design uses this feature because it enables an application programming interface to function as a complete self-describing entity which users can explore without help. The API provides internal links inside its responses which direct clients to their next destination.

The discovery endpoint at "/api/v1/info" provides links to both "/api/v1/rooms" and "/api/v1/sensors". The client application can begin at the root and use these links to explore the entire API without needing to know the URL structure beforehand. The system achieves reduced direct connection between the client and server because any URL change will result in the server updating the response link while clients who use links will automatically adjust their behavior without interruption. HATEOAS provides developers with up-to-date documentation that reduces integration problems and makes API usage easier to learn than static documentation.

-------------------------------------
Part 2: Room Management
-------------------------------------
Q1 : When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.

Answer:
Returning only IDs from a list endpoint is more efficient in terms of network bandwidth. When there are thousands of rooms because the response payload is much smaller. It forces the client to make an additional request for every room it wants details about known as the N+1 problem which increases latency and server load significantly.

Returning full room objects in the list response as implemented in this API, means the client receives all necessary data in a single request. This is better for client side processing because the application can immediately render or use the data without waiting for follow up requests. The trade off is a larger payload. But for a campus management system where clients regularly need full room details. Returning complete objects is the more practical and performant choice.

Q2 : Is the DELETE operation idempotent in your implementation? Provideadetailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

Answer: 
The execution shows that the DELETE function keeps its idempotent property because executing it multiple times results in the same outcomes. A user can execute the same request multiple times without affecting system results because it requires multiple requests to achieve identical outcomes. 

The API processes the DELETE request when a client sends "DELETE /api/v1/rooms/LAB-101" because the room exists but has no sensors. The server deletes the room and sends an HTTP 204 No Content response. The system removed the room from its database after executing the same request twice. The code provides an explicit solution for this situation. The system checks room status and returns HTTP 204 No Content when room status shows null instead of showing a 404 error. The client receives identical successful results because both room presence and absence conditions have been successfully tested which demonstrates idempotency. The server state after multiple identical DELETE requests is the same as after just one the room does not exist.

-------------------------------------
Part 3: Sensor Operations & Linking
-------------------------------------
Q1 : We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch

Answer : 
The annotation "@Consumes(MediaType.APPLICATION_JSON)" informs JAX-RS that the POST endpoint will process requests which use "application/json" as their "Content-Type" header value. JAX-RS rejects any client request which uses a content type different from "text/plain" or "application/xml" because it cannot process that type of request. The system automatically generates an HTTP 415 Unsupported Media Type response through its operational environment. The system blocks all endpoint data entries which the system cannot process through its sequential deletion mechanism. The system prevents all runtime errors during the processing of valid JSON data through the sensor creation process.

Q2 : You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering ands earching collections?

Answer :
Using "@QueryParam" for filtering (example, "/api/v1/sensors/type=CO2") is considered superior to embedding the filter in the path (example,"/api/v1/sensors/type/CO2") for several reasons.

Query parameters are optional by nature if the parameter is omitted. The endpoint simply returns all sensors making the filter entirely optional without needing a separate endpoint. Path parameters imply a specific resource identity. That means "/sensors/type/CO2" suggests that "type/CO2" is a unique resource identifier which is semantically incorrect. Query parameters can also be combined easily for multiple filters (example, "/type=CO2&status=ACTIVE"), whereas path based filtering becomes complex and unreadable with multiple criteria. Query parameters also align with REST conventions paths identify resources while query strings refine or filter them.

-------------------------------------
Part 4: Deep Nesting with Sub- Resources
-------------------------------------
Q1 : Discuss the architectural benefits of the Sub-Resource Locator pattern. How  does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class

Answer :
The Sub Resource Locator pattern allows a resource class to delegate handling of a sub path to a completely separate class. In this API the "SensorResource" class does not handle "/sensors/{id}/readings" directly. It has a locator method annotated with "@Path("/{id}/readings")" that returns a new instance of "SensorReadingResource" which handles all reading related operations.

The architectural benefit is separation of concerns and maintainability. If all endpoints including readings were defined in one massive "SensorResource" class. It would become extremely large, difficult to read and hard to maintain as the API grows. By delegating to a dedicated class each class has a single responsibility "SensorResource" manages sensors, "SensorReadingResource" manages readings. This mirrors real world software engineering principles such as the Single Responsibility Principle. It also makes testing easier as each class can be tested independently and new developers can understand the codebase more quickly because the structure mirrors the resource hierarchy.

-------------------------------------
Part 5: Advanced Error Handling, Exception Mapping & Logging
-------------------------------------
Q1 : Why is HTTP 422 often considered more semantically accurate than a standard 404 whenthe issue is a missing reference inside a valid JSON payload?

Answer :
The knowledge cut off is October 2023. The server returns the http 404 Not Found status because it cannot locate the requested URL resource within its stored resources. The client can send POST requests to the ("/api/v1/sensors") endpoint because the URL is correct and the sensors endpoint works as expected. The requirements require room data to be included in the request body because the room data will be used for future reference.

The most suitable status for this use case is http 422 Un-processable Entity because it shows that the server received a request containing valid JSON data with correct content type headers yet it failed to process the request because of validation errors in the payload. The system can provide feedback to the client developer about their data error while the endpoint operates without problems. The distinction allows client developers to quickly identify issues while developing better error handling routines for their application.

Q2 : From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Answer :
The practice of sharing unprocessed Java stack traces with outside API users creates major cybersecurity vulnerabilities. The stack trace reveals complete details about the application internal system which includes its package structure and all class and method identifiers and their corresponding location information. An attacker can use this information to identify which frameworks, libraries and versions are being used, then look up known vulnerabilities for those specific versions.

Stack traces provide complete access to all internal application functions. example, a NullPointerException with a stack trace pointing to a specific database query method tells an attacker exactly where input validation is
missing, which could be exploited with crafted inputs. The file paths that appear in traces show complete access to the server's directory structure. Hackers use stack traces as free reconnaissance tools which is why the Global Exception Mapper handles all unanticipated errors by sending a standard 500 response that contains no internal system information.

Q3 : Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single reource method?

Answer :
Using JAX-RS filters for cross-cutting concerns like logging is far superior to manually inserting"Logger.info()" statements in every resource method for several reasons.

First, Tt follows the DRY principle (Don't Repeat Yourself) with a filter, logging is written once in "CustomLogger.java" and automatically applies to every single request and response across the entire API including endpoints added in the future. If logging were done manually every new method would need its own log statements and forgetting to add them would create gaps in observability.

Second, Filters are guaranteed to run, they sit at the framework level and intercept all traffic, meaning even requests that result in errors or that hit unrecognized paths are still logged. Manual logging inside methods would miss any request that fails before reaching the method body.

Third, Keeping logging separate from business logic makes the code cleaner and easier to maintain. Resource methods focus purely on their business logic while the filter handles observability transparently.



