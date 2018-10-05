# Interview project

This repository contains the skeleton for a working Spring project. It should be modified to satisfy the additional features and objectives below. The skeleton includes a few things to get you started:

* A working spring-boot application.

* A basic database schema for managing users.

* Basic user creation.

	`POST /users`

* Basic user authentication.

	`POST /users/login`

	An authentication token will be returned that can be used on subsequest, authenticated requests. This token should be submitted in the header for authenticated requests: `X-Auth-Token`.

Please submit a pull-request to this repository so that Orb's engineering team can evaluate the additions. Feel free to ask us any questions about the project. We are very happy to help!

## Pre-requisites

* JDK 1.8+
* gradle 4.5+
* mysql 5.6.x

## Objective

We would like for you implement a simple currency exchange rate retrieval engine. It should utilize the [1forge API](https://1forge.com/forex-data-api/api-documentation) via their [free tier](https://1forge.com/forex-data-api/pricing). The endpoint should satisfy the follow requirements:

```
REQUEST:
GET /rates?from=USD&to=JPY

RESPONSE:
{
	"rate": 111.06
}
```

The new API endpoint should be able to serve at least 10,000 rate requests per day, and each rate should not be older than 5 minutes. It should also require a valid authentication token to be submitted to retrieve a rate.

We would like this endpoint to be compatible with three currencies:

* USD
* JPY
* EUR

## Run the tests

`gradle check`

## Run the application

`gradle bootRun`

Application will be listening on port: `18801`

## What we are looking for

* How you adapt to an unknown code base
* How well you complete the requirements
* Your design and implementation choices

## Notes

* Feel free to modify the already implemented base code if you think it is necessary.
* Feel free to use your own class design and patterns. You are not restricted to following our architecture.
* We dont mind if you use external dependencies, but keep in mind, we want to see how you architect and implement this project. The resulting pull-request should be representative of you and your process as much as possible.
* If you do not feel most comfortable java, feel free to use your preferred language, but the updated version should include the already included functionality: create user, acquire credentials, etc.
