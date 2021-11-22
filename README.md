# Interest

## Introduction
Microservice that calculate and generates the list of payments of the simple interest of a credit that must be paid in n terms and in weekly form.

## Database

This project use an embedded H2 for storing credit request and payments responses

## Documentation

This project uses SwaggerUI to facilitate endpoints usage. The documentation can be seen while project is running in `http://localhost:8080/swagger-ui/`

## Getting started

### Dependencies

This application has been proven to work with:
- Java version 8.0.292 or above
- Maven version 3.8.3 or above
- Docker version 20.10.8 or above

### Build

To build the project you can use the script `build` to build jar and push docker image:
```
bash build
```

### Run

To run the project you can use the script `run` to start the docker image and expose the application on `http>//localhost:8080`:
```
bash build
```

### Call Endpoints

The endpoints can be called using the following methods:
- The embedded Swagger Documentation in `http://localhost:8080/swagger-ui/`
- Import to Postman the collection under `src/main/resources/Interest.postman_collection.json`
- Or some other http tool such as cUrl

### Calculate Credit

In order to calculate credits, the following endpoint should be used:

```
curl --location --request POST 'http://localhost:8080/credits' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": 25000,
    "rate": 2,
    "terms": 52
}'
```

#### Body Params:
- amount [number, mandatory]: Amount to be paid in n weekly terms. Must be greater or equals than 1 and lesser or equals than 999999.00
- rate [number, mandatory]: Interest Rate to be applied to `amount` in order to calculate payment amounts. Can not have decimals. Must be greater than 1 and lesser than 100 
- terms [number, mandatory]: Number of weekly terms to pay credit. Must be greater or equals than 4 and lesser or equals than 52


### Health Check

In order to check if application is up, the following endpoint should be used:

```
curl --location --request GET 'http://localhost:8080/actuator/health'
```
