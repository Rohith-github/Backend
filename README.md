# Sample REST CRUD API with Spring Boot, Mysql, JPA and Hibernate 

## Steps to Setup

**1. Clone the application**


**2. Create Mysql database**
```bash
create database geo-fencing
```

**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties.dev`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the app using maven**

```bash
mvn package
java -jar target/spring-boot-rest-api-geofencing-0.0.1-SNAPSHOT

```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:9091>.

## Explore Rest APIs

The app defines following CRUD APIs.

    GET geocoord/maptype/{maptype}
    
    POST /geocoord
    
    GET /geocoord/names/maptype/{maptype}
    
    PUT /geocoord/id/{id}
    
    DELETE /geocoord/id/{id}


