# BackEnd Spring Boot Application

### Project Overview
This is service responsible for creating and tasks, it spawn task at scheduled interval.
it does have endpoints exposed in order to get the task and manipulate the tasks.  

### Search
Search Endpoint to search with Tasks and get reponse in Page.

url: POST /v1/tasks/search

request example : 
````
{
  "criteria": {
    "dueDateFrom": "2020-04-06T16:04:12.860Z",
    "dueDateTo": "2020-04-06T16:04:12.860Z",
    "status": "open"
  },
  "page": 0,
  "pageSize": 10,
  "sort": {
    "orders": [
      {
        "direction": "ASC",
        "property": "priority"
      }
    ]
  }
}
````

### Get
get endpoint to get Task by id
url: GET /v1/tasks/{id}

### Delete
Delete endpoint to delete Task by id
url: DELETE /v1/tasks/{id}

### Patch
Patch endpoint to patch any task by id

url: PATCH /v1/tasks/{id}

#### The add Operation
We use the add operation to add a new member to an object. Also, we can use it to update an existing member and to insert a new value.

For example, let's add description to the Task:
````
{
    "op":"add",
    "path":"/description",
    "value":"Hi And Hello"
}
````
#### The remove Operation
The remove operation removes a value at the target location.

For instance, let's remove the description of Task:

````
{
    "op":"remove",
    "path":"/description"
}
````
#### The replace Operation
The replace operation updates the value at the target location with a new value.

As an example, let's update the description of task:
````
{
    "op":"replace",
    "path":"/description",
    "value":"Bye Bye"
}
````

## Test
Application is configured to pen on port 8081 
so, open below url on any browser and you can test the api's using swagger 

[Test Api](http://localhost:8081/swagger-ui.html)

## Reports
Test report are available on 
- Code coverage can be seen by clicking -> [Jacoco Report](http://localhost:63342/schedular-app/scheduler-app-service/target/site/jacoco/index.html)
