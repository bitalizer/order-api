# Order Management System API

This API provides robust functionality for managing orders, products, and customers in your business. It offers features such as creating customers and products, placing orders, and searching for orders by date.

## Table of Contents

- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
  - [Installation](#installation)
- [Usage](#usage)
  - [Creating a Customer](#creating-a-customer)
  - [Creating a Product](#creating-a-product)
  - [Creating an Order](#creating-an-order)
  - [Modifying Order Line Quantity](#modifying-order-line-quantity)
  - [Searching Orders](#searching-orders)
- [Configuration](#configuration)

## Technology Stack

This project is built with the following technology stack:

- **Spring Boot 3.1.4:** A robust framework for building Java applications.
- **Hibernate:** An object-relational mapping (ORM) library for efficient database interactions.
- **Liquibase:** A database schema version control and migration tool.


## Installation

1. Clone this repository.

```shell
git clone https://github.com/bitalizer/order-api.git
```

## Usage

This API allows you to manage orders, products, and customers.

### Creating a Customer

To create a new customer, send a POST request to:

```
POST /api/v1/customers
```

```json
{
    "full_name": "John Doe",
    "email": "johndoe@example.com",
    "phone_number": "123-456-7890",
    "registration_code": "ABC123"
}
```

### Creating a Product

To create a new product, send a POST request to:

```shell
POST /api/v1/products
```

```json
{
    "name": "Sample Product",
    "sku_code": "SKU123",
    "unit_price": "19.99"
}
```

### Creating an Order

To place a new order, send a POST request to:

```shell
POST /api/v1/orders
```

Include the order details and order lines in the request body.

```json
{
    "order_lines": [
        {
            "quantity": 5,
            "product_id": 1
        },
        {
            "quantity": 31,
            "product_id": 2
        }
    ],
    "customer_id": 1
}
```

### Modifying Order Line Quantity

To change the quantity of products in an order line, send a PUT request to:

```shell
PUT /api/v1/orders/{order_id}/order-lines/{order_line_id}/quantity
```

Include the new quantity in the request body.

```json
{
    "quantity": 60
}
```

### Searching Orders

To search for orders, use the following endpoint with query parameters:

```shell
GET /api/v1/orders/search?product_id=1&customer_id=1&date_from=2023-01-01&date_to=2023-12-31
```

Modify the query parameters as needed to perform specific searches.

## Configuration

Before running the application, configure these environment variables for the database connection:

- **DB_HOST:** Hostname or IP address of the PostgreSQL server (default: `localhost`).
- **DB_PORT:** Port number for the PostgreSQL server (default: `5432`).
- **DB_NAME:** Name of the target database
- **DB_USER:** Database username
- **DB_PASSWORD:** Database user's password
