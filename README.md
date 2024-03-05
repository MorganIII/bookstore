# Bookstore API application 

## A fully functional bookstore application that is built on the REST architecture that user can search and order books as any online store.

## Technologies
- Java 17
- spring boot 3.3.0-SNAPSHOT
- postgresql
- Docker

## Features 
1. Authentication and Registration
      + register, login and logout functionalities
      + Account activation via email
2. searching and filtering
      + Search and filtering books by title and category
      + Sort books by popularity and last added
      + Pagination and limit
3. Cart Management
      + Add, remove and update items in the cart
      + View cart and clear cart
      + Apply coupon code
4. Order Management
      + Place an order
      + View orders history
      + Order tracking
5. Payment
      + Payment gateway integration
      + Different payment methods (Credit card, COD)
6. Admin Panel
      + Manage books, categories, orders, users
      + View and manage orders
      + View and manage users
      + View and manage books
      + View and manage categories
      + View and manage coupons

### order diagram

![order diagram](https://github.com/MorganIII/bookstore/blob/master/src/main/resources/images/order-sequence-diagram.svg)

## Installation
1. Clone the repository  
```git clone https://github.com/MorganIII/bookstore.git```
2. go to the [resources](https://github.com/MorganIII/bookstore/tree/master/src/main/resources) folder and add the application-dev.yml to configure the application
with the appropriate configurations. Here is an example of the application-dev.yml file:
```yml
#Database
POSTGRES_SQL_USERNAME: morgan
POSTGRES_SQL_PASSWORD: morgan
POSTGRES_SQL_PORT: 5432
POSTGRES_SQL_DB: bookstore

#Server
SERVER_PORT: 8080
ACTIVE_PROFILE: dev

#Email Config
EMAIL_HOST: localhost
EMAIL_PORT: 1025
EMAIL_ID: morgan@gmail.com
EMAIL_PASSWORD: morgan
VERIFY_EMAIL_HOST: http://localhost:${SERVER_PORT}

#Stripe keys
STRIPE_PUBLIC_KEY: pk_test_... #place it with your stripe public key
STRIPE_SECRET_KEY: sk_test_... #place it with your stripe secret key
STRIPE_WEBHOOK_SECRET: whsec_... #place it with your stripe webhook secret

# images path

IMAGES_PATH: #place it with the path to the folder you want to store the images
```
3. (Optional) If you want to use the email verification feature, you can use the [MailDev](https://github.com/maildev/maildev) to test it.
```bash
docker run -p 1080:1080 -p 1025:1025 maildev/maildev
```
4. Build and Run the application
```bash
mvn clean install -DskipTests
mvn spring-boot:run
```
5. Run section then category sql scripts under [sql folder](https://github.com/MorganIII/bookstore/tree/master/src/main/resources/sql) to add data to the database
and also run the below python script to add the book.csv data to the database
```python
import psycopg2
import pandas as pd
conn = psycopg2.connect(host='localhost',port=5432,dbname='bookstore',user='morgan',password='morgan')
cursor = conn.cursor()

df = pd.read_csv('book.csv', encoding='utf-8')

# Iterate through rows and execute parameterized queries
for i in range(1, len(df)):  # Skip header row
    book_title = df.iloc[i, 0]
    actual_price = df.iloc[i, 1].item()
    discount_price = df.iloc[i, 2].item()
    number_of_pages = df.iloc[i, 3].item()
    copies_in_stock = df.iloc[i, 4].item()
    books_to_be_printed = df.iloc[i, 5].item()
    sold_copies = df.iloc[i, 6].item()
    book_description = df.iloc[i, 7]  # Get book description
    book_thumbnail = df.iloc[i, 8]
    book_cover = df.iloc[i, 9]
    book_section = df.iloc[i, 10]
    book_category = df.iloc[i, 11]

    escaped_description = cursor.mogrify("quote_literal(%s)", (book_description,))[0]  # Remove extra quotes

    # Prepare and execute parameterized query
    query = """
        INSERT INTO book (
            book_title, actual_price, discount_price, number_of_pages,
            copies_in_stock, books_to_be_printed, sold_copies, book_description,
            book_thumbnail, book_cover, book_section, book_category, creation_time
        )
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, NOW())
    """

    cursor.execute(query, (
        book_title, actual_price, discount_price, number_of_pages,
        copies_in_stock, books_to_be_printed, sold_copies, escaped_description,
        book_thumbnail, book_cover, book_section, book_category
    ))


conn.commit()
cursor.close()
conn.close()

print("Data inserted successfully!")


```

6. The application will be running on [http://localhost:8080](http://localhost:8080) to view all the endpoints and test them, go to [swagger docs](http://localhost:8080/swagger-ui.html)

## Stripe
In order to use the payment gateway, you need to create an account on [Stripe](https://stripe.com/) and get the public and secret keys and the webhook secret key 
from [stripe dashboard](https://dashboard.stripe.com/test/apikeys) and add them to the application-dev.yml file described in the installation section. The integration
used with stripe is the [custom flow](https://docs.stripe.com/payments/accept-a-payment?platform=web&ui=elements) with [Collect payment details before creating an Intent scenario](https://docs.stripe.com/payments/accept-a-payment-deferred)
. In order to test the payment gateway, you can use the test card numbers provided by stripe [here](https://docs.stripe.com/testing#cards).

To simulate a successful payment follow the below steps:
1. sign in as a user
2. add items to the cart
3. go to [static folder](https://github.com/MorganIII/bookstore/tree/master/src/main/resources/static) and open the checkout.js file
and replace the stripe public key with your stripe public key and fill orderRequest object with the required data
4. open the checkout.html file and fill the form with the required data and click on the pay button
you can test card like 4242 4242 4242 4242 with any future date and any cvc number. 

> **Note:** you need to modify the `@CrossOrigin` annotation in the [OrderController](https://github.com/MorganIII/bookstore/blob/master/src/main/java/org/morgan/bookstore/controller/OrderController.java)
with the correct *origin* of your frontend application.
