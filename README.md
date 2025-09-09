# SilverBank

**SilverBank** is a backend banking service project built with Java and Spring Boot, designed to provide robust domain logic, secure authentication, and automated deployment for modern banking applications.

## Features

- **Account Management:** Create, update, and manage user accounts and balances.
- **Transaction Processing:** Secure money transfer, deposit, and withdrawal operations.
- **Authentication & Authorization:** User login and permission control, JWT-based authentication.
- **RESTful APIs:** Well-documented API endpoints for all major banking operations.
- **Test-Driven Development (TDD):** Comprehensive unit and integration tests ensure business logic reliability.
- **Automated Deployment:** CI/CD pipeline using GitHub Actions for seamless deployment to AWS EC2.
- **Systemd Integration:** Automated service restarts and stable operation via Linux systemd services.

## Tech Stack

- **Backend:** Java 17, Spring Boot, JPA
- **Database:** MySQL
- **DevOps:** AWS (EC2), GitHub Actions, systemd
- **Testing:** JUnit, Mockito
- **Version Control:** GitHub

## Getting Started

### Prerequisites

- Java 17+
- MySQL (local or remote)
- Maven

### Installation

1. **Clone the repository**
    ```bash
    git clone https://github.com/CastleSilver/SilverBank.git
    cd SilverBank
    ```

2. **Configure application properties**
    - Edit `src/main/resources/application-prod.yml` (or application.yml) with your database credentials.

3. **Build the project**
    ```bash
    ./mvnw clean package
    ```
    The JAR will be generated at `backend/build/libs/silver-0.0.1-SNAPSHOT.jar`.

4. **Run the application**
    ```bash
    java -jar backend/build/libs/silver-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
    ```

### API Documentation

- The project exposes RESTful endpoints for account and transaction operations.
- For full API details, refer to [API.md](API.md) (to be written, or describe endpoints inline).

## Testing

- Tests are located under `src/test/java/`
- To run all tests:
    ```bash
    ./mvnw test
    ```

## Deployment

- **CI/CD:** Automated via GitHub Actions. On push or PR merge to `main`, the latest JAR is built and deployed to AWS EC2.
- **Systemd:** The EC2 instance runs the service via a systemd unit file, ensuring auto-restart and stability.

## Project Highlights

- **TDD Applied:** All core banking logic is covered by unit and integration tests.
- **Automated Deployment:** GitHub Actions pipeline handles build, secure SSH file transfer, and remote service restart.
- **Production-Grade Setup:** Systemd and AWS integration for reliable operation and easy rollback.

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss proposed changes.

## License

[MIT](LICENSE)

## Author

[Seongeun Heo (CastleSilver)](https://github.com/CastleSilver)
