# Loan Request Management API

A simple REST API for managing personal loan requests developed for a banking entity. This API allows clients to submit loan applications, managers to review and modify their status, and the system to query the loan request history.

## Functional Requirements

### Overview
The banking entity requires a simple REST API to manage personal loan requests with the following core functionalities:

1. **Creation of a loan request**
2. **Modification of loan request status**
3. **Query existing loan requests**

### Detailed Requirements

#### 1. Loan Request Creation
The required data to create a loan request includes:
- **Applicant name**: Full name of the person requesting the loan
- **Requested amount**: The loan amount requested
- **Currency**: Currency for the loan (EUR, USD, GBP)
- **Identification document**: DNI, NIE, or similar identification
- **Creation date**: Automatically assigned when the request is created

#### 2. Query Functionality
The system must allow:
- **List all loan requests** with optional filtering by:
  - Status (PENDING, APPROVED, REJECTED, CANCELLED)
  - User ID
  - Currency
- **Query a specific loan request** by its ID

#### 3. Status Modification
The system must allow status modification following this workflow:
- **PENDING** → **APPROVED** or **REJECTED**
- **APPROVED** → **CANCELLED**
- **REJECTED** and **CANCELLED** are final states

### Status Flow Diagram
```
PENDING ──┐
          ├──► APPROVED ──► CANCELLED
          └──► REJECTED
```

## Technical Specifications

### Technology Stack
- **Java**: 17 (LTS)
- **Spring Boot**: 3.5.9
- **Spring Data JPA**: For database operations
- **PostgreSQL**: Primary database
- **Maven**: Build tool and dependency management
- **Swagger/OpenAPI**: API documentation

### Architecture
- **Controller Layer**: REST endpoints and HTTP request handling
- **Service Layer**: Business logic and validation
- **Repository Layer**: Data access with JPA
- **Model Layer**: Entity definitions and DTOs

### Database Schema
The application uses PostgreSQL with the following main entities:
- **User**: Stores user information (clients and managers)
- **LoanRequest**: Stores loan application data


## API Endpoints

### User Management
- `GET /api/users` - Get all users (with optional filters)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Loan Request Management
- `GET /api/loan-requests` - Get all loan requests (with optional filters)
- `GET /api/loan-requests/{id}` - Get loan request by ID
- `POST /api/loan-requests` - Create new loan request
- `PUT /api/loan-requests/{id}/status` - Update loan request status

## Setup and Installation

### Prerequisites
- **Java 17** or higher
- **Maven 3.6** or higher
- **PostgreSQL 12** or higher

### Database Setup
1. Install PostgreSQL
2. Create a database named `prestamos`
3. Update database credentials in `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/prestamos
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

#### Using Maven
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd prestamos
   ```

2. **Navigate to the application directory**
   ```bash
   cd app
   ```

3. **Install dependencies**
   ```bash
   mvn clean install
   ```


4. **Run the application**
  ```bash
  mvn spring-boot:run
  ```

  To run the application without running tests:
  ```bash
  mvn spring-boot:run -DskipTests
  ```

#### Alternative: Run JAR file
```bash
mvn clean package
java -jar target/loan-0.0.1-SNAPSHOT.jar
```

### Accessing the API
- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/v3/api-docs

## Development

### Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/project/loan/
│   │   │       ├── controllers/     # REST Controllers
│   │   │       ├── services/        # Business Logic
│   │   │       ├── repositories/    # Data Access Layer
│   │   │       ├── models/          # Entity Classes
│   │   │       └── dto/             # Data Transfer Objects
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
└── README.md
```

### Testing
Run tests with Maven:
```bash
mvn test
```

## Future Extensions

### Architectural Improvements
- **Hexagonal Architecture**: If project complexity grows, consider implementing hexagonal architecture (ports and adapters) to improve maintainability and testability
  - Refine DTO and entity separation, and centralize mapping logic (e.g., using MapStruct) to decouple domain and transport layers
- **Microservices**: Split into dedicated services for user management, loan processing, and notification handling
- **User Role-based Access**: Limit features and access based on user type (e.g., admin, manager, client) if required for security and business logic.

### Security Enhancements
- **IAM Platform Integration**: Manage users through dedicated Identity and Access Management platforms (e.g., Keycloak, Auth0, Azure AD)
- **JWT & OAuth2**: Implement JWT token-based authentication and OAuth2 for secure API access
- **API Gateway**: Add API gateway for centralized authentication, rate limiting, and routing

### Observability & Monitoring
- **Centralized Logging**: Implement structured logging with tools like ELK Stack (Elasticsearch, Logstash, Kibana) or EFK (Elasticsearch, Fluentd, Kibana)
- **Application Monitoring**: Add APM tools (Application Performance Monitoring) like Prometheus, Grafana, or New Relic
- **Distributed Tracing**: Implement tracing with tools like Jaeger or Zipkin for microservices environments

### DevOps & Deployment
- **CI/CD Pipeline**: Implement automated build, test, and deployment pipelines using:
  - **Jenkins**, **GitHub Actions**, or **GitLab CI/CD**
  - Automated testing, security scanning, and code quality checks
- **Containerization**: 
  - **Docker**: Containerize applications for consistent deployment environments
  - **Kubernetes/OpenShift**: Orchestrate containers for scalability and high availability
- **Infrastructure as Code**: Use tools like **Terraform** or **Ansible** for infrastructure management
- **Blue-Green Deployments**: Implement zero-downtime deployment strategies

### Additional Features
- **Event-Driven Architecture**: Use message brokers (Apache Kafka, RabbitMQ) for asynchronous communication
- **Caching**: Implement Redis or Hazelcast for improved performance
- **Database Optimization**: Add read replicas, connection pooling, and query optimization
- **API Versioning**: Implement proper API versioning strategy for backward compatibility

### Business & Functional Extensions
- **Credit Scoring Integration**: Connect with external credit bureaus and scoring systems
- **Automated Risk Assessment**: AI/ML-powered risk evaluation algorithms
- **Document Management**: File upload and management system for loan documentation
- **Loan Calculator**: Real-time interest calculation and payment scheduling
- **Notification System**: 
  - Email/SMS notifications for status changes
  - In-app notification center
- **Analytics & Reporting**:
  - Business intelligence dashboards
  - Loan performance metrics
  - Risk analysis reports
  - Regulatory compliance reporting
- **Audit Trail**: Complete activity logging for regulatory compliance
- **Multi-Currency Support**: Advanced currency conversion and management
- **Collateral Management**: Track and manage loan guarantees and assets
- **Payment Integration**: Connect with payment gateways for loan disbursement and collection
- **Status Change Timestamps**: Store the date and time of each status change for loan requests, not just the current status.

### Advanced Technical Features
- **Rate Limiting**: API throttling to prevent abuse and ensure fair usage
- **Circuit Breaker Pattern**: Resilience patterns for external service dependencies
- **Data Encryption**: 
  - End-to-end encryption for sensitive data
  - Database encryption at rest
  - TLS/SSL for data in transit
- **Backup & Recovery**: 
  - Automated database backups
  - Point-in-time recovery capabilities
  - Disaster recovery procedures
- **Performance Optimization**:
  - Database query optimization
  - Connection pooling
  - Response compression
- **Health Checks & Readiness Probes**: Kubernetes-ready health endpoints
- **Metrics Collection**: Custom business and technical metrics
- **Feature Flags**: Dynamic feature toggling for A/B testing and gradual rollouts
- **Data Validation**: Advanced validation rules and business constraints
- **Batch Processing**: Scheduled jobs for bulk operations and maintenance


## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
