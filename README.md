# **Task Manager**

The app is being created for personal use. It allows adding, editing and deleting tasks, changing statuses and priorities, loading and saving data in the database, as well as authentication of users. I am currently developing the backend part. Frontend will be written in either AngularJS or React. Both layers will be communicating using REST API.

**The applications is _IN PROGRESS_**. 

Functionalities implemented so far:
* endpoints with CRUD methods for all entities
* getting tasks assigned to particular category
* validation of data
* scheduling of task status
* exception handling
* integration with H2 database
* API documentation by Swagger
* pagination

Functionalities to be implemented:
* authentication provided by Spring Security
* unit tests
* integration with MySQL or PostgreSQL
* frontend of the app
* deployment to the public server (e.g. Heroku)

## **Technology used so far:**
* Java 11
* SpringBoot
* Spring Data JPA
* Hibernate
* H2

## **How to run:**
1. Clone the repository onto your own computer.

2. Go to the main folder of the project and run this command:

* for the Unix system:
```
./mvnw spring-boot:run
```
* for the Windows CMD:
```
mvnw.cmd spring-boot:run
```

3. Go to the following page in your browser to test the app: [http://localhost:8080/](http://localhost:8080/)

4. Go to the following page in your browser to see database tables: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

5. Use default values to log in to H2 database:

   JDBC URL: *"jdbc:h2:mem:testdb"*

   username: *"sa"*

   password: *"[blank]"*
