# **Task Manager**

The app allows adding, editing and deleting tasks, changing statuses and priorities, loading and saving data in the database, as well as authentication and registration of users. I am currently developing the backend part. Frontend will be written in the future. Both layers will be communicating using REST API.

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
* authentication and authorization by Spring Security
* sending emails with confirmation links

Functionalities to be implemented:
* one-to-many relation between users and tasks (each user can see his own tasks list)
* integration with MySQL or PostgreSQL
* deployment to the public server (e.g. Heroku)
* unit tests

## **Technology used so far:**
* Java 11
* SpringBoot
* Spring Security  
* Spring Data
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

3. Go to the following page in your browser to test the app: [http://localhost:8080/api](http://localhost:8080/api)

4. Go to the following page in your browser to see database tables: [http://localhost:8080/api/h2-console](http://localhost:8080/api/h2-console)

5. Use default values to log in to H2 database:

   JDBC URL: *"jdbc:h2:mem:testdb"*

   username: *"sa"*

   password: *"[blank]"*
