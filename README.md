
## User Management System : Deploying a Spring Boot Application with PostgreSQL Using Docker, GitHub Actions, and AWS EC2

This is a User Management System built with Spring Boot, PostgreSQL, Docker, and Docker Compose. It includes GitHub Actions for continuous deployment to an EC2 server. This project provides a REST API for creating, updating, retrieving, and deleting user information.

For a detailed step-by-step guide on deploying a Spring Boot application with PostgreSQL using Docker, GitHub Actions, and AWS EC2, you can refer to [this blog post](https://tmrtechworld.com/index.php/2022/09/26/deploying-a-spring-boot-application-with-postgresql-using-docker-github-actions-and-aws-ec2/).


### Table of Contents
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Docker and Docker Compose](#2-docker-and-docker-compose)
  - [3. Environment Variables](#3-environment-variables)
  - [4. Run the Application](#4-run-the-application)
- [Deployment](#deployment)
  - [GitHub Actions](#github-actions)
  - [EC2 Deployment](#ec2-deployment)
- [API Endpoints](#api-endpoints)


### Technologies Used
- **Spring Boot:** Backend framework for building the REST API.
- **PostgreSQL:** Database for storing user information.
- **Docker & Docker Compose:** Containerization for consistent environments.
- **GitHub Actions:** CI/CD for automating deployment to EC2.
- **AWS EC2:** Hosting the application on an EC2 instance.

### Features
- User creation, retrieval, update, and deletion.
- Input validation with custom exception handling.
- Continuous deployment to AWS EC2 using GitHub Actions.
- Dockerized setup for easy local development and testing.

### Prerequisites
- **Docker** and **Docker Compose** installed on your machine.
- **Java 17** (or compatible) installed.
- An **AWS EC2 instance** with Docker installed for deployment.

### Setup Instructions

#### 1. Clone the Repository
```bash
git clone https://github.com/tanmay031/user_management.git
cd user_management
```

#### 2. Docker and Docker Compose
Ensure Docker and Docker Compose are installed on your local machine. This project uses Docker to run both the Spring Boot application and PostgreSQL.

#### 3. Environment Variables
Create a `.env` file in the root directory to store environment variables used by Docker Compose. For example:
```env
POSTGRES_USER=yourusername
POSTGRES_PASSWORD=yourpassword
POSTGRES_DB=yourdatabase
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/yourdatabase
```

> **Note:** The `db` host refers to the PostgreSQL container defined in the Docker Compose file.

#### 4. Run the Application
Build and start the application using Docker Compose:
```bash
docker-compose up --build
```
This command:
- Builds the Docker images for the Spring Boot application and PostgreSQL.
- Starts the containers, with the application accessible at `http://localhost:8080`.

To stop the application, use:
```bash
docker-compose down
```

### Deployment
The project uses GitHub Actions to automate the deployment process to an EC2 instance.

#### GitHub Actions
1. **CI/CD Pipeline:** GitHub Actions is configured to build, test, and deploy the application automatically on push to the main branch.
2. **Configuration:** The GitHub Actions workflow is defined in `.github/workflows/build-and-deploy.yml`.

#### EC2 Deployment
1. **EC2 Setup:** Ensure your EC2 instance has Docker installed and ports 80 (HTTP) and 8080 are open.
2. **SSH Access:** The GitHub Actions workflow requires SSH access to the EC2 instance to deploy the Dockerized application.
3. **Deployment:** On every push to the main branch, GitHub Actions will:
  - On push to the main branch: Trigger the workflow whenever changes are pushed to the main branch.
  - Build the Docker image: Build the Docker image and tag it with the current Git commit SHA for versioning.
  - Push the Docker image to Docker Hub: Push the built Docker image to Docker Hub.
  - Connect to the EC2 instance: Use SSH to connect to the EC2 instance.
  - Use Docker Compose on EC2: Pull the latest Docker image from Docker Hub and restart the container using docker-compose.

### API Endpoints
- **POST** `/api/users` - Create a new user.
- **GET** `/api/users/{id}` - Retrieve user by ID.
- **PUT** `/api/users/{id}` - Update user information.
- **DELETE** `/api/users/{id}` - Delete user.
-  **GET** `/api/users` - Retrieve all users with pagination and sorting.
  - **Query Parameters:**
    - `page` (optional, default = 0): The page number to retrieve (zero-based).
    - `pageSize` (optional, default = 10): The number of users per page.
    - `sortBy` (optional, default = "id"): The field by which to sort users.


