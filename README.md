# BankSystem

A console-based banking application developed with **Java 21**, following a layered architecture and using **Hibernate ORM** for database persistence.

The project was built as a backend-focused Java application to practice object-oriented programming, database integration, business logic,
validation, testing, and clean separation of responsibilities.

## Features

- Customer management
  - Create customers
  - Update customers
  - Search customers
  - Soft delete customers
  - Support for real and legal customers
- Account management
  - Create, update, search, and delete accounts
  - Deposit
  - Withdraw
  - Transfer
  - Balance management
- Multi-currency support
  - USD
  - EUR
  - GBP
  - Currency conversion using `BigDecimal`
- ATM functionality
  - Customer login
  - Balance checking
  - Cash withdrawal
  - Banknote/ATM stock management
- Validation and custom exception handling
- Password encoding
- JSON data export
- Hibernate ORM persistence
- H2 database
- Optimistic locking with `@Version`
- Unit and service-layer testing
- Mockito-based mocking

## Architecture

The application follows a layered architecture:

```text
Console UI
    ↓
Facade
    ↓
Service
    ↓
DAO
    ↓
Hibernate ORM
    ↓
H2 Database
