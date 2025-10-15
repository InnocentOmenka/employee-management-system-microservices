# Employee Management System (Microservices Architecture)

This project is a RESTful Employee Management System built using Spring Boot microservices.  
It demonstrates a complete HR platform with authentication, service discovery, centralized configuration, and routing through an API Gateway.

## 🏗️ Microservices Included
1. **Discovery Service** – Eureka Server for service registration and discovery (port 8761).
2. **Config Server** – Centralized configuration management for all services (port 8888).
3. **API Gateway** - Request routing, security, and Swagger UI aggregation (port 8080)
4. **Auth Service** – Handles authentication, JWT generation, and role-based access (port 8081).
5. **Employee Management Service** – CRUD operations and manages employees and departments (port 8082).

## ⚙️ System Requirements
- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL 14+**
- **IntelliJ IDEA
- **Spring Cloud Netflix Eureka**
- **Spring Cloud Config**
- **Docker (optional)**

## Tech Stack

- **Framework**: Spring Boot 3.2.0 + Spring Cloud 2023.0.0
- **Discovery**: Netflix Eureka
- **Gateway**: Spring Cloud Gateway
- **Config**: Spring Cloud Config Server
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL + Flyway + JPA/Hibernate
- **Documentation**: Springdoc OpenAPI 3
- **Build**: Maven
- **Java**: 17

## Prerequisites

- **Java 17+** (OpenJDK recommended)
- **Maven 3.8+**
- **PostgreSQL 13+** (port 5432, database: `employee_db`)
- **Git** (for future Git-based config)

## Setup Instructions


### 1. Database Setup & Initial Seeding

1. **Start PostgreSQL** and create database:
   ```sql
   CREATE DATABASE employee_db;
First Run - Automatic Admin Seeding:

2. When Auth Service starts for the first time, the UserSeeder automatically creates:

   -Email: admin@company.com
   -Password: Admin@123
   -Role: ADMIN


This admin user is only created once (checks for existing email)
Subsequent runs: Seeder skips if admin exists (logs "Admin user already exists")


User Hierarchy:

ADMIN (auto-seeded): Can create MANAGERS and EMPLOYEES
MANAGER: Created by ADMIN, can manage department employees
EMPLOYEE: Created by ADMIN/MANAGER, read-only access

1. **Start PostgreSQL** and create database:
   ```sql
   CREATE DATABASE employee_db;

## 🚀 How to Run the Project (Local)

1. **Clone the repository**
   ```bash
   git clone https://github.com/InnocentOmenka/employee-management-system-microservices.git
   cd employee-management-service

**Start Services (Sequential Order)**
   Step 1: Start Discovery Service (Eureka)
   Step 2: Start Config Server
   Step 3: Update Microservices for Config Client
        For each service (auth-service, employee-management-service, api-gateway): Add Config Client dependency to pom.xml

<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-config</artifactId>
</dependency>


   Step 4: Start Auth Service
   Step 5: Start Employee Management Service



API Endpoints
Base URLs

Gateway: http://localhost:8080
Auth Service: http://localhost:8080/auth/**
Employee Service: http://localhost:8080/employee/**

Authentication Service Endpoints
Login (Public)

POST /auth/api/v1/auth/login
Request:
{
"email": "admin@company.com",
"password": "Admin@123"
}

Response:
{
"status": "success",
"message": "Login successful",
"data": {
"token": "eyJhbGciOiJIUzI1NiJ9...",
"user": {
"id": 1,
"email": "admin@company.com",
"role": "ADMIN"
}
}
}

Register (Public)

POST /auth/api/v1/auth/register
Headers: Content-Type: application/json
Request:
{
"firstName": "John",
"lastName": "Doe",
"email": "john@company.com",
"password": "Password123",
"role": "EMPLOYEE",
"departmentId": 1
}


Employee Management Service Endpoints
(All require Authorization: Bearer <token>)
Get All Employees (ADMIN/MANAGER)

GET /employee/api/v1/employees

Get Employee by ID (All roles)

GET /employee/api/v1/employees/{id}

Create Employee (ADMIN)

POST /employee/api/v1/employees
Request:
{
"firstName": "Alice",
"lastName": "Green",
"email": "alice@company.com",
"role": "EMPLOYEE",
"departmentId": 1
}


Role-Based Access Control (RBAC)

ADMIN: Full CRUD on employees/departments
MANAGER: Read employees in their department
EMPLOYEE: Read-only access to own data

JWT tokens include role claim for authorization.

**Department-Manager Assignment**
Key Feature: Managers are assigned to specific departments, enabling department-scoped access control.

Create Department (ADMIN only)
Create Manager User (via Auth Service, ADMIN only)
Assign Manager to Department (ADMIN only)
Manager Access: Can only view employees in their assigned department
