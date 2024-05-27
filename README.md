# Starship API

A CRUD API for managing spaceships from movies and series - W2M Test

## Table of Contents

- [Versions](#versions)
- [Objective](#objective)
- [Credentials](#credentials)
- [Run Project](#run-project)
- [Postman Tests](#postman-tests)
- [Docker](#docker)

## Versions

Versions of the used technologies:

- Java 21
- Spring 3.2.5

## Objective

The goal of this project is to create an API that contains a CRUD for starships featured in various movies and series. Key features include:
- Controller file handling requests.
- Service layer executing request requirements, interacting with an H2 Database using Spring H2 and Spring JPA.
- Centralized exception handling.
- Basic authentication using Spring Security.
- Logging using @Aspect and caching using @Cache.
- Unit and integration tests covering most of the API code and basic functionality.
- Docker configuration for containerization.
- API documentation using Swagger.

## Credentials

- Username: `user`
- Password: `w2m`

## Run Project

Instructions on how to run the API:

Simply run the main file. Ensure you have selected `StarshipApiApplication`.

## Postman Tests

Here are the necessary cURLs and HTTP requests to try the API using Postman:

**Note:** You may need to manually add the credentials to execute a request. To do so, go to the Authorization tab, select Basic Auth in type, and enter the credentials mentioned above.

### Post a Starship

```bash
<details>CURL<details>
curl --location 'http://localhost:9090/api/starships' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic dXNlcjp3Mm0=' \
--data '{
  "name": "x-wing 1",
  "pilot": "Luke Skywalker"
}'
<details>
<summary>Request Body (JSON)</summary>
json
{
  "name": "x-wing 1",
  "pilot": "Luke Skywalker"
}
</details>
```

```bash
<details>HTTP REQUEST<details>
POST http://localhost:9090/api/starships
```

### Get all starships (with pagination)

```bash
<details>CURL<details>
curl --location 'http://localhost:9090/api/starships?page=0&size=10&sortBy=id&sortOrder=asc'
```

```bash
<details>HTTP REQUEST<details>
GET http://localhost:9090/api/starships?page=0&size=10&sortBy=id&sortOrder=asc
```

### Get starship by ID

```bash
<details>CURL<details>
curl --location 'http://localhost:9090/api/starships/1'
```

```bash
<details>HTTP REQUEST<details>
GET http://localhost:9090/api/starships/1
```

### Update an existing starship

```bash
<details>CURL<details>
curl --location --request PUT 'http://localhost:9090/api/starships/1' \
--header 'Content-Type: application/json' \
--data '{
"name": "wing",
"pilot": "Luke Skywalker"
}'
<details>
<summary>Request Body (JSON)</summary>
json
{
    "name": "x-wing",
    "pilot": "Han Solo"
}
</details>
```

```bash
<details>HTTP REQUEST<details>
PUT http://localhost:9090/api/starships/1
```

### Delete starship

```bash
<details>CURL<details>
curl --location --request DELETE 'http://localhost:9090/api/starships/1'
```

```bash
<details>HTTP REQUEST<details>
DELETE http://localhost:9090/api/starships/1
```

## Docker

To load a docker image, you will need to run the following commands:
```bash
docker build --no-cache -t starship-api .
```
```bash
docker run -p 9090:9090 starship-api
```

**Note:** Have in mind that the API runs in the port 9090. To lift up the rabbitMQ instance, run "docker compose up", The credentials are "guest", "guest". You can fin the UI at "http://localhost:15672/"





