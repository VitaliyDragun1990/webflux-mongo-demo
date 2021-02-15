# Spring WebFlux application with MongoDB database

## Description
   Simple microservice-style application built using Spring Boot with WebFlux reactive framework. It provides REST API with several endpoints built
in functional style and uses DTOs to communicate with the outside world. Exception handling and input validation uses
functional approach as well.
   The microservice saves its data in the MongoDB database which is represented as a separate service.
   The application works with two types of objects, called nodes. The first one, NodeRoot, has an id, and a name attributes.
The second one, NodeDesc, has the same attributes as NodeRoot and, in addition, a description attribute. Both objects are
stored in the same MongoDB collection called "node".

## Structure
   The whole application consists of two services - WebFlux-powered API backend service which communicates with MongoDB service.
   
## WebFlux-powered API backend endpoints - "http://host_ip_address/v1/nodes"

#### Get all nodes: [GET http://host_ip_address:8080/v1/nodes]
   - Return all available nodes
```json
[
    {
        "id": "602a237f4e2a955c46fdc73b",
        "name": "Simple node 1"
    },
    {
        "id": "602a28064e2a955c46fdc73c",
        "name": "Simple node 2",
        "description": "Some description"
    }
]
```  

#### Get node by ID: [GET http://host_ip_address:8080/v1/nodes/{node_id}]
   - Return node with provided identifier, response status - 200 OK
```json
{
    "id": "602a237f4e2a955c46fdc73b",
    "name": "Simple node 1"
}
``` 
   - In case no node exists with provided identifier, response status - 404 NOT FOUND
```json
{
    "timestamp": "2021-02-15T07:59:02.096121",
    "status": 404,
    "error": "Not Found",
    "message": "Fail to find node with id:[{}]",
    "path": "/v1/nodes/602a237f4e2a955c46fdc73bd"
}
``` 

#### Delete the existing node by ID: [DELETE http://host_ip_address:8080/v1/nodes/{node_id}]
   - Delete node with provided identifier, response status - 200 OK, no body
    
```
```
   - In case no node exists with provided identifier, response status - 404 NOT FOUND
```json
    {
        "timestamp": "2021-02-15T07:59:02.096121",
        "status": 404,
        "error": "Not Found",
        "message": "Fail to find node with id:[{}]",
        "path": "/v1/nodes/602a237f4e2a955c46fdc73bd"
    }
```

#### Create a new node: [POST http://host_ip_address:8080/v1/nodes]
   - Request should contain json with required 'name' attribute, and optional 'description' attribute:
```json
{
    "name": "Node with description",
    "description": "Some description"
}
```
   - In case of success - response status 201 CREATED and response body with the id of newly created node
```json
{
    "id": "602a237f4e2a955c46fdc73b"
}
``` 
   - In case of failure - response status 400 BAD REQUEST and response body with failed request details
```json
{
    "timestamp": "2021-02-15T08:08:14.466048",
    "status": 400,
    "error": "Bad Request",
    "message": "node name is required",
    "path": "/v1/nodes"
}
```

#### Update the existing node: [PUT http://host_ip_address:8080/v1/nodes/{node_id}]
   - Request should contain json with required 'name' attribute, and optional 'description' attribute:
```json
{
    "name": "Node with description",
    "description": "Some description"
}
```
   - In case of success - response status 200 OK with empty body
```json
{
    "id": "602a237f4e2a955c46fdc73b"
}
``` 
   - In case of failure - response status 400 BAD REQUEST and response body with failed request details
```json
{
    "timestamp": "2021-02-15T08:08:14.466048",
    "status": 400,
    "error": "Bad Request",
    "message": "node name is required",
    "path": "/v1/nodes/602a237f4e2a955c46fdc73b"
}
```

## Build and run
   - In order to build and run this application, you must have a system with previously installed JDK with a version no lower than 11.0.10.
docker and docker-compose must be installed too.
   - In order to build the WebFlux backend API microservice along with initial docker image for it, open your terminal in the project root directory
and execute the following command:
````
./mvnw clean package
````
   - In order to make docker run the WebFlux backend API microservice along with MongoDB service, open your terminal in the project root directory
and execute the following command:
````
docker-compose -f ./docker/docker-compose.yml up -d
````
  - In order to make docker stop the WebFlux backend API microservice along with MongoDB service and remove theirs containers, open your terminal
in the project root directory and execute the following command:
````
docker-compose -f ./docker/docker-compose.yml down -v
````
