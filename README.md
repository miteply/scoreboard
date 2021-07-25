
# Scoreboard

The scoreboard is the application for store, retrieve, update sport events data  
and once the data has been saved or updated, the server will notify the browser.


## Features

- Create and update events
- Obtain all sport events and the last created or updated event
- Push notification to the client after persistence.


## Technologies

 - Java 11
 - Maven 4.0.0
 - Spring boot 2.5.2
 - H2 Database
 - JavaScript
 - Bootstrap 4.3.1
 - HTML, CSS


 ## Project Structure
- Class SportEvent corresponds to the table of sportevents in the database
- Class SportEventRequestDto encapsulates data, it is used by controller to transfer data between itself and client
- Class UtilMapper converts from Entity to DTO and from DTO to Entity
- Interface SportEventRepo extends JpaRepository for working with SportEvent persistence. It will autowired in SportEventServ
- Service SportEventServ implements repository methods and custom finder methods. It will we autowired in web controller
- Service NotifyServ to create connection with the client and notify him when the new data is inserted/updated in the database
- Web controller SportEventCtrl has request mapping methods for RESTful requests sush as: getAll, getById, getLastUpdated, create, update, getLastUpdated
- Web controller SseCtrl to expose endpoint for clients to create the connection.
- Web controller WelcomeCtrl to manage the root path endpoint and return index.html


# APIs

| Methods                                   | Urls                       | Actions 
| :------------                             |:---------------            |:-----   
| GET      | /api/events                    | retrieve all events        |
| GET      | /api/events/:id                | retrieve the event by :id  |
| GET      | /api/events/last               | retrieve the last event    |
| GET      | /api/events/?name=:name        | retrieve the event by :name|
| POST     | /api/events                    | create a new SportEvent    |
| PATCH    | /api/events                    | update a SportEvent by:id  |

### Reference

## Get all sport events

```http
  GET /api/events
```
Return the list of all sport events.

## Get all events by name

```http
  GET /api/events?name=value
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `name`      | `string` | **Required**. in URL |

## Get event

```http
  GET /api/events/id
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. in URL |
| `Version`      | `string` | returned by server in **Response Header**.|

## Get last event

```http
  GET /api/events/last
```
Return the last updated event.

## Create event

```http
  POST /api/events
```
**Require** the following JSON in body request.

{

    "teamHome": "Misha",
    "teamAway": "Jonik",
    "scoreHome": 1,
    "scoreAway": 0
} 

**Response** 
- Header : "Location" (endpoint of created resource)
- Body:
{
    "id": 1,
    "version": 0,
    "teamHome": "Misha",
    "teamAway": "Jonik",
    "scoreHome": 1,
    "scoreAway": 0,
    "updatingDate": "2021-07-25 23:03:48"
    
}


## Update event

```http
  PATCH /api/events/id
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. in URL |
| `version`      | `string` | **Required**. in request header |

Update only the values passed in the body request only if the client's version of the resource 
is equal to server version.

### Installation && Tests


## Run Locally

Clone the project

```bash
  git clone https://github.com/miteply/scoreboard.git
```

Go to the project directory

```bash
  cd scoreboard
```

Run the jar file

```bash
  java -jar scoreboard-0.0.1-SNAPSHOT.jar
```

Verify the deployment by navigating to your server address in
your preferred browser.

```sh
http://127.0.0.0:8080
```

## Testing with POSTMAN
Test in POSTMAN by importing the [json file](https://github.com/miteply/scoreboard/blob/main/scoreboardUrlTest.postman_collection.json) 
