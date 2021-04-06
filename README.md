# Excel file upload

## About
Upload large Excel files and convert them. 
- backend: The backend REST API application is implemented with Spring Boot.
- frontend: The frontend is an Angular application.

For building the project Gradle is used.

## Preparation
Install required software
- Java 11 (https://adoptopenjdk.net/)
- Node.js (https://nodejs.org/en/)
- Angular CLI (https://cli.angular.io/)

## Run the application

### Start the backend (REST API) application:
This will download Gradle, build the application and start it.
```
./gradlew bootRun
```

### Start the frontend (SPA) application:

Install all dependencies:
```
cd file-upload-frontend
npm install
```

Start the Angular application:
```
ng serve
```

Open http://localhost:4200/ in your browser.