# Book Lending Application

### Overview

- Solution to "College Library Project" : Problem Statement 3
  (Book Lending Application)
- Submission by team Dockerators (Kalyan Ram, Vidhish T, Vikas K)
- This repository contains the code to the backend of our book lending application. It contains the REST endpoints that are queried by the frontend.

### Functionality

- There are some predefined admins (with admin credentials) who can add and delete librarians (with librarian credentials)
- Librarians can then login to the application and have access to 2 pages:
- On the Student Page:
  - The user can select a student from a list of students. They can also add, update and delete students (APIs from our solution to problem statement 1 will be queried)
  - The user can then view the books borrowed by the student, issue a book to that student and return a book that was borrowed by that student.
- On the Books Page:
  - The user can select a book from a list of books. They can also add, update and delete books (APIs from our solution to problem statement 2 will be queried)
  - The user can then view the student that borrowed that book, return the book and issue that book to a student.

### Auth Endpoints

- The following are the API endpoints for all user authentication and authorization (Admin and Librarian).
  | Method | Endpoint | Description |
  |--------|----------|--------------|
  |GET |/api/admin/getLibrarian | Returns a list of librarians, i.e users with the role LIBRARIAN. Only a user with ADMIN role can access this route. |
  |POST| /api/admin/addLibrarian | Adds a librarian, i.e a user with the role LIBRARIAN. Only a user with ADMIN role can access this route.|
  |DELETE |/api/admin/deleteLibrarian/{email} | Deletes the librarian with the given email id. Only a user with ADMIN role can access this route.| 
  |POST| /api/users/signup | This route allows to add a user. Everyone has access to this route.|
  |POST |/api/users/login | This route is used to verify the credentials of an user. Return a jwt token which the user can use to access the remaining endpoints. Everyone has access to this route.|  
  |GET |/api/users/logout | Logs out the user. Everyone has access to this route.| 

### Auth Project Structure
- To implement authentication, the project uses spring security maven dependency to authenticate and authorize users. When a request is received by the backend, before executing the service code of the route, the auth layer catches the request, and then using the jwt token, i.e if sent to get the user credentials.
- Once the credentials are obtained, if they are not valid, then an error is returned, else the role of the user is checked. This is implements using separate filters, where the first filter checks for the validity of the user and the second checks the role.
- The role used to verify if the user has authorization to access a route.
- The above is implemented in a config directory where the securityconfig file contains all the filters needed to verify a user and the filters are implemented in the same directory.
- A JWtservice class is used in the filters to execute the funtionalities described above. This class allows to generate a jwt token when a user logs in and also has funtions to get the credentials of the user.

### Student Endpoints

- The following are the API endpoints for performing CRUD operations on students in our application. These APIs query external APIs (from the Student App):
  | Method | Endpoint | Description |
  |--------|----------|--------------|
  |GET |/api/booklending/students | Returns all students that have been added to the database so far. |
  |GET| /api/booklending/students/rollNo/{rollNo} | Returns the student that corresponds the rollNo in the path variable.|
  |POST |/api/booklending/students | Takes a student object in the body of the request and adds it to the database.|
  |PUT| /api/booklending/students | Takes a student object in the body of the request and updates the student in the DB that corresponds to the rollNo of the student in request body to the student in the request body.|
  |DELETE |/api/booklending/students/rollNo/{rollNo} | Deletes the student that corresponds the rollNo in the path variable from the DB.|


### Book Endpoints

- The following are the API endpoints for performing CRUD operations on books in our application. These APIs query external APIs (from the Books App):
  | Method | Endpoint | Description |
  |--------|----------|--------------|
  |GET |/api/booklending/books | Returns all books that have been added to the database so far. |
  |GET| /api/booklending/books/code/{code} | Returns the book that corresponds the code in the path variable.|
  |POST |/api/booklending/books | Takes a book object in the body of the request and adds it to the database. |
  |PUT| /api/booklending/books | Takes a book object in the body of the request and updates the book in the DB that corresponds to the code of the book in the request body to the book in the request body.|
  |DELETE |/api/booklending/books/code/{code} | Deletes the book that corresponds the code in the path variable from the DB.|

### Book Lending Endpoints

- We have a BookLendingEntity Class which has the following fields:
  - transactionId
  - rollNo (of the student)
  - bookCode (of the book)
  - issued (boolean : true => the book is issued, false => the book has been returned)
  - issueDate
  - returnDate
- The following are the API endpoints for lending and returning books to students and viewing the books that are issued by a student and the student that issued a book.
  | Method | Endpoint | Description |
  |--------|----------|--------------|
  |POST |/api/booklending/lendBook | Takes a bookLendingEntity in the body of the request and issues the book of the given code to the student of the given roll number. Sets issued to true, issueDate to today and returnDate to null and saves the bookLending Entity to the database. Returns an error if the student or the book does not exist or if the book is already lent to someone.|
  |PUT| /api/booklending/returnBook/{transaction_id} | Returns the book that corresponds the transaction id in the path variable. Finds the bookLendingEntity of the given id and the issued variable to true from the database, sets issued to false and the return date to today and saves it in the DB.|
  |GET |/api/booklending/getBook/{rollNo} | Returns all the books that have been borrowed by a student of the given roll number. Returns an error if there is no student corresponding to the given roll number. |
  |GET| /api/booklending/getStudent/{code} | Returns the student that has borrowed the book corresponding to the given code (if there is a student). Returns an error if there is no book corresponding to the code.|


### Book-lending Project Structure

- Follows the Controller-Service-Repository pattern.
  - The controller layer is responsible for management of the REST interface to the business logic. It defines the REST API endpoints and calls the respective service layer functions based on which endpoint is invoked. It uses the @RestController annotation to make the application RESTful.
  - The service layer handles the business logic implementation. There is an interface for the book service and is then extended with a Book Service Implementation. This is in accordance to the SOLID Principles. Logs are made here in the different service functions using the slf4j logger class to denote different successes and failures.
  - The repository layer handles all interaction with the database. Our application uses the JPA library to make database operations easier to code.


### Testing

- The Testing is performed in the following way (using JUnit to automate the tests):
    - Create unit test cases for the bookLendingEntity method in the repo layer. These tests use the in memory H2 Database to avoid affecting the actual production database. They test the different lending and returning functionalities.
    - Create unit test cases for each method in the service layer. Mock the repo layer (using Mockito) to isolate the business logic. Test different user and book Lending scenarios and edge cases to validate the business rules.
 

### Deployment 

- We used various AWS services to deploy the application. Firstly, we created an AWS that contained multiple EC2 instances to run the docker image. A repository was created for this repo to store the images that are built by the CI/CD process using the ECR service. Then a task-definition was made which had information to run the image. In the cluster, we created a service which would make use of the task definition built and run the container on one of the EC2 instances.
- We used the RDS database for storing data, the endpoint and password of the database is added github secrets along with the access key id and secret access key.
- We used the CI/CD infrasture that github provides to create a workflow file which contained instructions to deploy the application on aws.
- Added the task-definition.json file that we got in the previous step to the root of the repo.
- Configured the aws.yml file with cluster name, service name, region ,repository and the location of the task-definition file.
- A Dockerfile was added ot the root of the repo. The Dockerfile contains commands to setup , test and build the image that will be run on the EC2 instance. The instance will run only if the command running tests passes.
- Once deployed, the ip address of the app is visible on the task that is running the app.
Push all the changes to github using git. Once pushed, github will take care of deploying to aws.

