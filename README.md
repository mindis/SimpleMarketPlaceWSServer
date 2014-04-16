=========================
# “Simple Market Place” to demonstrate stateful web services
=========================

## Introduction


Developed “Simple Market Place” for demonstrating stateful web services. This includes a web
service client and a web service server and provides below functionalities.
1. User sign In, sign Up, sign out
2. User can create ad which includes their email Id and “seller information” provided by them.
3. The ads created by the user is visible to all the users who signIn into the application.
4. Users can buy products displayed by other users.
5. Users can browse through product depending on the category selected by the user.
6. Users can add/remove product to/from shopping cart.
7. Users are required to enter sixteen digit card number for checkout
8. User can add new category.
9. User can add new products in the category
10.Users can check the history of products bought and sold by them in “My History”.
11.Users last login time is displayed when the user logs in.

## System Design

This application provides a platform to the user for buying and selling the products. They
can get the list of item sold and bought by them.

##Operating Environment

The application runs on Apache Tomcat v6.0 application server. Uses Apache Axis web service
framework which provides implementation of SOAP server and various utilities and APIs for
generating and deploying web service applications. MySQL database is used for storing all the
information related to the application like product, category etc.

##Files and Database Design

Web Service Client uses WSDL file provided by the web service and according to that the client
will create all the corresponding class required by the client for accessing web service methods.
The server maintains it's data in MySQL database. Below diagram gives an idea about the database
design of the application.

Database Design : Database contains four tables as USER, CATEGORY, PRODUCT and
SHOPPING_CART. They share references to each other accordingly.

## Human-Machine Interfaces

As the user signIn into the application he is redirected to catalog page. Catalog page fetch all
the product by default and shows all the items to the user. User can choose to browse through this
list of all categories or can select the desired category from the dropdown provided in the menu bar.
User can add/remove items from the catalog to/from the shopping cart. He can also choose the
quantity of the item he wants to buy. He can proceed to checkout by clicking on the checkout button
provided in the menu bar. Shopping cart page displays all the items added by the user to the
shopping cart, its quantity and total cost of the all products that need to be paid by the user. He can
enter his 16 digit credit card number in the textbox provided on the right hand side on the shopping
cart page. Upon clicking on make payment button the user will receive a success notification.
Any type of User can create an ad by adding a product and providing the information about that
product. When other users buy the product created by seller user their counts are updated. The
status of bought and sold products are visible in “My History” section of the the application.

## Processing Logic :

All communication between the client and server takes place using SOAP messages. When a
user logs-in the user object is stored in the session and is used for validating the user whenever he
request any kind of service from the server. Every time a request is made by the the user it is been
forwarded to the web service client servlets which then decides the flow of that request. i.e. if it
finds that the user is not logged in then it will redirect it to the sign in page else it will invoke web
service method and display the output to the user.
When user add the item to the shopping cart it makes an request to the web service server for
adding it to the shopping cart. Server takes the product details and execute a SQL query to insert it
into the database. Similarly, for all the database operations the client make an request to the web
service server.

Basic validations are performed on client side only before the request is made to the web
service client and notified to the user which decreases the number of request to the client. The
request is made only after the primary validation is passed by the application.
