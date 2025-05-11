# PC Builder Application

This project is a full-stack PC Builder application that allows users to configure and optimize PC builds based on budget and purpose. It consists of a **React frontend**, a **Spring Boot backend**, and a **PostgreSQL database**, all containerized using Docker.

---

## Features
- **Frontend**: Built with React, TypeScript, and Vite for a fast and interactive user experience.
- **Backend**: Developed with Spring Boot to handle API requests and business logic.
- **Database**: PostgreSQL for storing component data.
- **Containerization**: Docker and Docker Compose for easy deployment and management.

---

## Prerequisites
Before setting up the project, ensure you have the following installed:
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: Comes bundled with Docker Desktop.
- **Node.js**: [Install Node.js](https://nodejs.org/) (if running the frontend locally).
- **Java 17**: [Install Java 17](https://adoptium.net/) (if running the backend locally).

---
## Screenshots
![image](https://github.com/user-attachments/assets/68515156-46cb-4888-af89-1a1f6a021338)
![image](https://github.com/user-attachments/assets/5f578bf5-a2e3-4d60-bd64-8cb55394220b)
![image](https://github.com/user-attachments/assets/ce39fa83-604c-408d-8492-d8da9dbde0bd)

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/pcbuilder.git
cd pcbuilder
```
### 2. Environment Configuration
#### Backend
- The backend uses the following environment variables (configured in `docker-compose.yml`):
  - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://postgres:5432/pcdb`
  - `SPRING_DATASOURCE_USERNAME`: `pcuser`
  - `SPRING_DATASOURCE_PASSWORD`: `pcpass`

#### Frontend
- The frontend uses the `REACT_APP_API_URL` environment variable to connect to the backend. This is set in `docker-compose.yml`:
  ```yaml
  environment:
    - REACT_APP_API_URL=http://backend:8080
### 3. Build and Run with Docker
1. Build and start all services (frontend, backend, and database):
   ```bash
   docker-compose up --build
2. Access the application:
    - Frontend: http://localhost:3000
    - Backend API: http://localhost:8080/api
