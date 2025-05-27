# Turbolessons

Turbolessons is a comprehensive cloud-based platform designed for music teachers to efficiently manage their teaching activities. The platform enables teachers to handle billing, schedule lessons, and share educational content with their students.

## Overview

Turbolessons provides a suite of features to streamline the music teaching process:

- **User Authentication**: Secure login and role-based access control using Okta
- **Lesson Scheduling**: Create, update, and manage lesson schedules
- **Billing and Payments**: Subscription-based billing integrated with Stripe
- **Content Management**: Upload and share documents and video content
- **Real-time Messaging**: WebSocket-based chat between teachers and students
- **Automated Notifications**: Email reminders for lessons and billing

## Architecture

Turbolessons is built on a microservices architecture, with each service handling a specific aspect of functionality:

![Turbolessons Architecture](turbolessons_diagram.png)

### Microservices

- **Config Server**: Centralized configuration management using Spring Cloud Config
- **Service Registry**: Service discovery with Eureka Server
- **API Gateway**: Request routing and load balancing with Spring Cloud Gateway
- **Admin Service**: User authentication and role management with Okta integration
- **Event Service**: Lesson scheduling and calendar management
- **Message Service**: Real-time chat functionality using WebSocket
- **Email Service**: Automated email notifications and reminders
- **Video Service**: Video content management with Google Cloud Storage integration
- **Payment Service**: Billing and subscription management with Stripe integration
- **Turbolessons Frontend**: React-based user interface

## Technology Stack

### Backend
- Java 11+
- Spring Boot
- Spring Cloud (Config, Gateway, Eureka)
- Spring WebFlux
- MySQL Database
- WebSocket for real-time communication

### Frontend
- React
- Redux for state management
- Material UI components
- Axios for API communication

### DevOps & Infrastructure
- Docker for containerization
- CI/CD pipelines
- Prometheus and Grafana for monitoring
- ELK stack for logging

## Getting Started

### Prerequisites
- JDK 11 or higher
- Node.js and npm
- Docker and Docker Compose
- IDE (IntelliJ IDEA/Eclipse for backend, VS Code for frontend)

### Setup and Installation
1. Clone the repository
   ```
   git clone https://github.com/your-organization/turbolessons.git
   cd turbolessons
   ```

2. Start the infrastructure services
   ```
   docker-compose up -d config-server service-registry api-gateway
   ```

3. Start the backend services
   ```
   docker-compose up -d admin-service event-service message-service email-service video-service payment-service
   ```

4. Start the frontend application
   ```
   cd turbolessons-frontend
   npm install
   npm start
   ```

## Development

### Project Structure
```
turbolessons/
├── config-server/
├── service-registry/
├── api-gateway/
├── admin-service/
├── event-service/
├── message-service/
├── email-service/
├── video-service/
├── payment-service/
├── turbolessons-frontend/
└── docker-compose.yml
```

### Building and Testing
- Build all services: `./mvnw clean install`
- Run tests: `./mvnw test`
- Build Docker images: `docker-compose build`

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://reactjs.org/)
- [Okta](https://www.okta.com/)
- [Stripe](https://stripe.com/)
- [Google Cloud Storage](https://cloud.google.com/storage)
