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
  |GET |/api/admin/getLibrarian |  |
  |POST| /api/admin/addLibrarian | |
  |DELETE |/api/admin/deleteLibrarian/{email} | | 
  |POST| /api/users/signup | |
  |POST |/api/users/login | |  
  |GET |/api/users/logout | | 

### Auth Project Structure

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

### Book and Student Endpoints Project Structure

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
