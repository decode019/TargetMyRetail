
# myRetail

Case Study myRetail Rest API for Target

## Tech Stack

Intellij, Maven, Java 8, Spring, Spring Boot, MongoDB

## Project

myRetail is a rapidly growing company with HQ in Richmond, VA and over 200 stores across the east coast. myRetail wants to make its internal data available to any number of client devices, from myRetail.com to native mobile apps.

The goal for this exercise is to create an end-to-end Proof-of-Concept for a products API, which will aggregate product data from multiple sources and return it as JSON to the caller.

Your goal is to create a RESTful service that can retrieve product and price details by ID. The URL structure is up to you to define but try to follow some sort of logical convention.

Build an application that performs the following actions:

-   Responds to an HTTP GET request at /products/{id} and delivers product data as JSON (where {id} will be a number.

-   Example product IDs: 15117729, 16483589, 16696652, 16752456, 15643793)

-   Example response: {"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}

-   Performs an HTTP GET to retrieve the product name from an external API. (For this exercise the data will come from redsky.target.com, but let’s just pretend this is an internal resource hosted by myRetail)

-   Example: [http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics](http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics)

-   Reads pricing information from another pricing api (https://price. myRetail.com/product/{productId}/price) and combines it with the product id and name from the HTTP request into a single response. Price API can be a mock API

-   BONUS: Accepts an HTTP PUT and POST request at the same path (/products/{id}), containing a JSON request body similar to the GET response, and updates the product’s price in the data store.

## Build and Run

### Setup/Run Server

-   We need two databases. One for testing and one for production.

### In MongoDB :

-   Create the first database named "productsDB" with a collection named "products"
-   Create the second database named "productsTestDB" and with a collection named "products"

### Setup/Run Project

-   Clone/download this repository into your favorite folder --> open in favorite IDE that has Maven support --> clean & build --> run