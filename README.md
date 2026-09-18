# 🎮 In-Game Ad Engine (In Progress)

![Java CI with Maven](https://github.com/uncleshu1974/java-spring-boot-learning/actions/workflows/maven.yml/badge.svg)

Welcome to the **In-Game Ad Engine** public showcase! 
This repository contains the production-ready modules of a larger, ongoing project designed to deliver dynamic, targeted advertisements directly inside video games.

## 🎯 What this project will do (End Goal)
When fully completed, the In-Game Ad Engine will allow game developers to integrate a lightweight SDK into their games, automatically requesting and displaying ads (banners, videos, billboards) without disrupting gameplay. Advertisers will use a dedicated portal to upload creatives, set budgets, and target specific gaming demographics.

The system relies on a **microservice architecture** built with Spring Boot, ensuring high availability and independent scaling.

## ✅ What is currently Working & Tested
Currently, this repository showcases the completed **Authentication Service (`auth-service`)**, which acts as the secure gateway for advertisers.

### 🛡️ Auth Service Features (Production-Ready)
- **Advanced JWT Authentication**: Secure, stateless session management.
- **Refresh Token Rotation**: Long-lived refresh tokens stored in the database with strict expiration logic to maintain UX without compromising security.
- **2FA & Password Recovery (Real SMTP)**: Full integration with JavaMailSender to send real OTP codes and temporary reset tokens via email.
- **Rate Limiting**: Brute-force protection on authentication endpoints powered by `Bucket4j`.
- **Session Management**: Ability to view and manually revoke active sessions/devices connected to an advertiser's account.
- **Interactive Documentation**: Fully documented APIs via Swagger UI/OpenAPI 3.

## 🚀 How to Run the Auth Service
1. Clone this repository.
2. Rename `.env.example` to `.env` and fill in your MySQL and SMTP credentials.
3. Run `docker-compose up -d` to spin up the database.
4. Launch the application (e.g., via Maven: `./mvnw spring-boot:run` inside the `auth-service` folder).
5. Visit `http://localhost:8084/swagger-ui/index.html` to interact with the APIs.
5. Visit `http://localhost:8084/swagger-ui/index.html` to interact with the APIs.

## 🧠 Design Decisions & Trade-offs
To build a scalable and testable system, several architectural decisions were made:
- **CORS Wildcard (`*`) in AuthController**: Currently set to wildcard for local development and testing across various frontend clients. In a production environment, this will be restricted to the specific domains of the Advertiser Portal and the Ad-Serving SDK via Spring Security config.
- **Refresh Token Rotation**: Instead of extending the life of a standard JWT, the system uses a strict rotation mechanism for Refresh Tokens stored in the database. This allows for manual revocation of compromised sessions.
- **Testing with H2 in-memory Database**: To ensure fast and reliable integration tests that don't depend on complex local Docker networks (especially within VS Code Dev Containers), the system uses an H2 in-memory database configured for MySQL compatibility. This guarantees that tests can be executed seamlessly in any local environment or CI/CD pipeline.
- **Repository Sync Rule**: This public repository (`InGame-Ad-Engine-InProgress`) contains ONLY code that is 100% completed, tested, and validated. Active development happens in a private repository, and features are promoted here only when fully stabilized.
## 🚧 What's Next?
The next module in development is the **Ad Campaign Service**, which will handle the core business logic (budgets, creatives, and ad-serving algorithms). Stay tuned for updates!
