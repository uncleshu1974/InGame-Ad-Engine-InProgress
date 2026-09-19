# 🎮 In-Game Ad Engine (Development Repository)

> **⚠️ PRIVATE REPOSITORY:** This is the main development workshop for the In-Game Ad Engine project. Code here is constantly evolving and may contain work-in-progress features.

## 🌟 Project Vision
The **In-Game Ad Engine** is a robust, microservice-based architecture designed to seamlessly serve, track, and manage dynamic advertisements within video games. It acts as a bridge between game developers and advertisers, providing a highly scalable backend to manage campaigns, budgets, and real-time ad delivery.

## 🏗️ Architecture
The project is built on **Java Spring Boot 3** and utilizes a Polyrepo/Monorepo structure orchestrated via Docker Compose.

### Microservices:
1. **Auth Service (`auth-service`)**: Handles the onboarding and authentication of Advertisers. It features Enterprise-grade security including JWT, long-lived Refresh Tokens, 2FA via real SMTP, Rate Limiting (Bucket4j), and Session Revocation.
2. **Ad Campaign Service (`ad-campaign-service`)**: (Work in Progress) Will handle the core business logic—managing advertiser budgets, ad creatives, campaign targeting, and serving the actual ads to the game clients via high-performance APIs.

## 🚀 Getting Started (Development)
1. Copy `.env.example` to `.env` and fill in your database and SMTP credentials.
2. Run `docker-compose up -d` to start the MySQL database container.
3. Start the individual microservices via your IDE or Maven (`./mvnw spring-boot:run`).

## 🗺️ Roadmap
- [x] Database schema design and entity generation.
- [x] Auth Service: Basic JWT authentication.
- [x] Auth Service: SMTP email integration and 2FA.
- [x] Auth Service: Rate Limiting and Session management.
- [ ] Ad Campaign Service: Campaign CRUD operations.
- [ ] Ad Campaign Service: Ad Delivery API for game clients.
- [ ] Analytics & Tracking implementation.
