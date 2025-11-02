# **Spring Boot Banking Application**

## **Overview**
The **Spring Boot Banking Application** is a backend system built to simulate essential banking operations such as account creation, customer management, deposits, withdrawals, and transaction tracking.  
This project integrates **Spring Boot**, **Spring Security (JWT)**, **Spring Data JPA**, **Email Notification**, and **DTO–Entity Mapping** to create a secure, modular, and scalable architecture.

It is designed for learning, demonstration, and practical use cases in secure banking APIs.

---

## **Project Structure**

### 1. Configuration
- **SecurityConfig.java** — Configures Spring Security with JWT authentication and authorization rules.
- **JwtFilter.java** — Validates JWT tokens for each request.
- **JwtService.java** — Handles token generation, validation, and extraction of user details.

### 2. Controller
- **AuthController.java** — Manages user registration and login APIs.
- **CustomerController.java** — Handles customer CRUD operations.
- **AccountController.java** — Manages account operations such as deposit, withdraw, and transaction retrieval.

### 3. Entity
- **User.java** — Represents application users with roles.
- **Customer.java** — Represents customers with account relationships.
- **Account.java** — Represents customer accounts with balance details.
- **Transaction.java** — Logs all transaction activities for audit and tracking.

### 4. DTO & Mapper
- **DTO Classes:** Used to transfer data safely between layers without exposing entity details.
- **Mapper Classes:** Convert between Entity and DTO objects using manual mapping or libraries like MapStruct.

### 5. Service
- **AuthService.java** — Manages authentication and token generation.
- **AccountService.java** — Implements business logic for deposits, withdrawals, and transactions.
- **EmailService.java** — Sends notifications via email for actions like registration or transaction updates.

### 6. Repository
- Uses **Spring Data JPA** for database access (CustomerRepository, AccountRepository, TransactionRepository, UserRepository).

### 7. Exception Handling
- **GlobalExceptionHandler.java** — Centralized handler for validation, authentication, and business exceptions.

---

## Key Features
- Secure **JWT-based authentication** for login and API access.
- Role-based access control (User / Admin).
- **Email notification** support for user registration and transactions.
- **DTO and Mapper pattern** for clean and decoupled data transfer.
- Comprehensive **exception handling** using `@ControllerAdvice`.
- **Deposit** and **withdrawal** APIs with balance validation.
- **Transaction history tracking** per account.
- Integrated **Spring Security** for endpoint protection.
- Supports **MySQL** (for production) and **H2** (for testing).

---

## **API Endpoints**

**Authentication**

**Method	Endpoint	Description**

**POST**	/api/auth/register --	Register a new user (sends confirmation email)

**POST**	/api/auth/login	-- Login and generate JWT token

## **Customer Management**

**Method	Endpoint	Description**

**POST**	/api/customers --	Create a new customer

**GET**	/api/customers/{id} --	Get customer by ID

**GET**	/api/customers --	Get all customers

## **Account Operations**

**Method	Endpoint	Description**

**POST**	/api/accounts	-- Create a new account for a customer

**GET**	/api/accounts/{id} --	Get account details

**POST**	/api/accounts/deposit	 -- Deposit money into an account

**POST**	/api/accounts/withdraw --	Withdraw money (validates balance)

**GET**	/api/transactions/account/{id} --	Retrieve transaction history by account 
