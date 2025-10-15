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

ADMIN (auto-seeded): Can create MANAGERS and EMPLOYEES.
MANAGER: Created by ADMIN, can manage department employees.
EMPLOYEE: Created by ADMIN, read-only access.

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

**Gateway: http://localhost:8080**

**Auth Service: http://localhost:8080/auth/**

**Employee Service: http://localhost:8080/employee/**

###**Authentication Service Endpoints**
Login (Public)

##POST 

/auth/api/v1/auth/login

##Request:

{
"email": "admin@company.com",
"password": "Admin@123"
}

##Response:

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

##Register (Public)

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


**Role-Based Access Control (RBAC)**

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


#### **Direct Service Swagger UI Access**

### **Individual Service Swagger UIs**

Each microservice exposes its own **Swagger UI** directly on its service port:

- **Auth Service Swagger UI**:  
  **`http://localhost:8081/swagger-ui/index.html`**  
  📋 **Endpoints**: Authentication, login, user registration, JWT generation

- **Employee Management Service Swagger UI**:  
  **`http://localhost:8082/swagger-ui/index.html`**  
  📋 **Endpoints**: Employee CRUD, department management, manager assignments

### **Access Instructions**

1. **Start all services** following the sequential startup order
2. **Navigate directly** to each service's Swagger UI:
   - Auth: `http://localhost:8081/swagger-ui/index.html`
   - Employee: `http://localhost:8082/swagger-ui/index.html`
3. **Test endpoints** with proper JWT authorization:
   - Login via Auth Swagger UI → Copy JWT token
   - Use token in Employee Swagger UI (Authorize button)

### **Direct Access Benefits**
- ✅ **Service Isolation**: Test individual services independently
- ✅ **Debugging**: Identify service-specific issues
- ✅ **Development**: Direct endpoint testing without gateway
- ✅ **Configuration Verification**: Check `springdoc` settings per service

### **Note on Direct Access**
- **CORS**: Services have individual CORS config (`spring.web.cors`)
- **JWT**: Tokens must be manually copied between services
- **Routing**: Bypasses API Gateway - use for development/debugging only
- **Production**: Use API Gateway (`http://localhost:8080`) for unified access

### **Service-Specific Testing Workflow**
1. **Auth Service** (`8081/swagger-ui`):
   - Login with `admin@company.com` / `Admin@123`
   - Create managers/employees
   - Copy JWT token
   
2. **Employee Service** (`8082/swagger-ui`):
   - Authorize with JWT from Auth Service
   - Test department creation and manager assignment
   - Verify RBAC and department scoping

**No aggregated Swagger UI available** - each service maintains its own OpenAPI documentation and Swagger interface.

## **🏛️ Design Patterns & SOLID Principles Applied**

### ***Design Patterns***

#### **1. Microservices Architecture Pattern**
- **Single Responsibility**: Each service handles one domain (Auth, Employee Management)
- **Loose Coupling**: Services communicate via REST APIs through Gateway
- **Independent Deployment**: Services can be scaled/updated separately

#### **2. Service Registry & Discovery Pattern**
- **Eureka Client/Server**: Dynamic service registration and load-balanced discovery
- **Client-Side Load Balancing**: `lb://service-name` routing via Spring Cloud LoadBalancer
- **Health Checks**: Services register with heartbeats, deregister on failure

#### **3. API Gateway Pattern**
- **Unified Entry Point**: Single gateway for all client requests
- **Protocol Translation**: REST to service-specific protocols
- **Cross-Cutting Concerns**: CORS, security, rate limiting centralized
- **Service Orchestration**: Route aggregation and Swagger proxying

#### **4. Externalized Configuration Pattern**
- **Centralized Config**: Config Server with Git/Native backend
- **Environment Profiles**: `dev`, `prod` configs without code changes
- **Dynamic Refresh**: `/actuator/refresh` for runtime config updates
- **12-Factor App**: Config stored in environment (Config Server)

#### **5. CommandLineRunner Pattern (User Seeding)**
- **Database Initialization**: Automatic admin user creation on first startup
- **Idempotent Operations**: Seeder checks for existing admin before creating
- **Separation of Concerns**: Seeding logic isolated from main application

#### **6. Repository Pattern**
- **Data Access Abstraction**: `UserRepository`, `EmployeeRepository` interfaces
- **CRUD Operations**: Standardized data access across services
- **JPA Integration**: Spring Data JPA repositories with custom queries

#### **7. Builder Pattern**
- **Complex Object Creation**: `User.builder()` for fluent entity construction
- **Immutable Objects**: Builder ensures valid state before entity creation
- **Readability**: Clean, readable entity initialization

#### **8. JWT Stateless Authentication Pattern**
- **Token-Based Auth**: No server-side session storage
- **Claims-Based Authorization**: Role and user ID embedded in token
- **Scalability**: Horizontal scaling without sticky sessions

#### **9. RBAC (Role-Based Access Control) Pattern**
- **Department-Scoped Access**: Managers limited to assigned departments
- **Hierarchical Permissions**: ADMIN > MANAGER > EMPLOYEE
- **Junction Table**: `department_managers` for many-to-many relationships

### **SOLID Principles Applied**

#### **🔤 S - Single Responsibility Principle**
- **Auth Service**: Only handles authentication and user management
- **Employee Service**: Only manages employee/department CRUD
- **Config Server**: Only serves configuration files
- **Gateway**: Only handles routing and cross-cutting concerns
- **UserSeeder**: Only responsible for initial admin creation

#### **🔤 O - Open/Closed Principle**
- **Extensible Routing**: Gateway routes easily extended via `application.yml`
- **Plugin Architecture**: Custom filters can be added without modifying core
- **Profile-Based Config**: New environments added without code changes
- **Repository Interfaces**: New data sources without changing service logic

#### **🔤 L - Liskov Substitution Principle**
- **Repository Abstraction**: Any JPA repository implementation interchangeable
- **Service Interfaces**: Mock implementations for testing
- **JWT Utilities**: Different token providers substitutable
- **Eureka Clients**: Alternative discovery services (Consul, Zookeeper)

#### **🔤 I - Interface Segregation Principle**
- **Focused Interfaces**: `UserRepository` only exposes user operations
- **DepartmentService**: Specific methods for department operations
- **JWT Util**: Only token extraction/validation methods
- **No Fat Interfaces**: Clients not forced to implement unused methods

#### **🔤 D - Dependency Inversion Principle**
- **Inversion of Control**: Spring `@Autowired` and constructor injection
- **Repository Injection**: Services depend on abstractions, not concrete classes
- **Config Client**: Services depend on Config Server abstraction
- **JWT Dependency**: Services use `JwtUtil` interface, not concrete implementation

### **Additional Enterprise Patterns**

#### **Circuit Breaker Pattern** (Future Enhancement)
```java
// Resilience4j integration planned
@CircuitBreaker(name = "auth-service")
public Mono<User> getUser(String token) { ... }
```

#### **Bulkhead Pattern** (Future Enhancement)
- **Thread Pool Isolation**: Separate thread pools per service
- **Resource Protection**: Prevent cascading failures

#### **Event-Driven Architecture** (Future Enhancement)
- **Kafka/RabbitMQ**: Async communication between services
- **Event Sourcing**: Audit trail for employee changes
- **CQRS**: Separate read/write models for reporting

#### **12-Factor App Methodology**
1. **Codebase**: One codebase per service, multiple deploys
2. **Dependencies**: Declared in `pom.xml`, no system dependencies
3. **Config**: Externalized via Config Server
4. **Backing Services**: DB, Eureka, Config Server treated as attached resources
5. **Processes**: Stateless services, horizontal scaling
6. **Port Binding**: Services bind to configured ports
7. **Concurrency**: Thread-per-request model via Spring WebFlux/Servlet
8. **Disposability**: Fast startup/shutdown for scaling
9. **Dev/Prod Parity**: Same configs via profiles
10. **Logs**: Structured logging to stdout
11. **Admin Processes**: Seeding via `CommandLineRunner`
12. **API First**: OpenAPI specs for contract testing

### **Domain-Driven Design Elements**

#### **Bounded Contexts**
- **Auth Bounded Context**: User authentication, roles, JWT
- **Employee Bounded Context**: Employee lifecycle, departments, manager assignments
- **Gateway Bounded Context**: Routing, security, aggregation

#### **Entities & Value Objects**
- **User Entity**: Identity, email, role, password hash
- **Employee Entity**: Employee-specific attributes, department association
- **Department Entity**: Department details, manager assignments

#### **Aggregates**
- **Department Aggregate**: Department + assigned managers + employees
- **User Aggregate**: User + roles + department assignments

#### **Domain Services**
- **DepartmentService**: Business logic for department operations
- **UserService**: Authentication and user lifecycle
- **JwtUtil**: Token validation and claims extraction

### **Clean Architecture Influence**
- **Entities**: Core domain models (`User`, `Employee`, `Department`)
- **Use Cases**: Service layer with business logic (`DepartmentService`)
- **Interface Adapters**: Controllers convert HTTP to service calls
- **Frameworks/Drivers**: Spring Boot, JPA, Eureka as external concerns

### **Security Patterns**
- **JWT Pattern**: Stateless authentication with claims
- **OAuth2 Resource Server** (Future): Standardized token validation
- **Principal Propagation**: JWT forwarded through gateway to services
- **Method-Level Security**: `@PreAuthorize` for fine-grained access control

This comprehensive application of **design patterns** and **SOLID principles** ensures:
- **Scalability**: Independent service scaling and deployment
- **Maintainability**: Clear separation of concerns and single responsibilities
- **Testability**: Dependency injection and interface-based design
- **Flexibility**: Extensible configuration and routing
- **Reliability**: Service discovery and health checks
- **Security**: RBAC with department scoping and JWT validation

---

Add this section to your README under **"Architecture"** or **"Design Principles"** to showcase the professional software engineering practices applied in your microservices system!

---

Add this section to your **"API Endpoints"** or **"Testing"** section. This clarifies that there's **no gateway aggregation** and users should access Swagger UI directly on each service port.
