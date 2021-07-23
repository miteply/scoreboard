
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


 ## Project Structure
- Class DefaultSportEventLoader to save initial data in the database
- Class SportEvent corresponds to the table of sportevents in the database
- Class SportEventRequestDto encapsulates data, it is used by controller to transfer data between itself and client
- Class UtilMapper converts from Entity to DTO and from DTO to Entity
- Interface SportEventRepo extends JpaRepository for working with SportEvent persistence. It will autowired in SportEventServ
- Service SportEventServ implements repository methods and custom finder methods. It will we autowired in web controller
- Service NotifyServ to create connection with the client and notify him when the new data is inserted/updated in the database
- Web controller SportEventCtrl has request mapping methods for RESTful requests sush as: getAll, getById, getLastUpdated, create, update, getLastUpdated
- Web controller SseCtrl to expose endpoint for clients to create the connection.
- Web controller WelcomeCtrl to manage the root path endpoint and return index.html


## APIs

| Methods                                   | Urls                       | Actions 
| :------------                             |:---------------            |:-----   
| GET      | /api/events                    | retrieve all events        |
| GET      | /api/events/:id                | retrieve the event by :id  |
| GET      | /api/events/last               | retrieve the last event    |
| GET      | /api/events/?name=:name        | retrieve the event by :name|
| POST     | /api/events                    | create a new SportEvent    |
| PUT      | /api/events                    | update a SportEvent by:id  |


## Installation
- Open chrome browser
- Download and unzip the source repository https://github.com/miteply/scoreboard.git
- Change directory inside the Project
- Run the app form command line using the command ( ./mvnw spring-boot:run )

## Usage

After running the application you should see the opened browser with the welcome page of the web app where you can create a new sports event and see the push notification when the object has been saved in the database.
