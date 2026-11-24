# Secure Email Authentication Gateway

## Project Overview

The Secure Email Authentication Gateway is a Java-based web application designed to analyze email authentication results and provide an overall security assessment.

It helps users understand email authentication status through three possible outcomes: PASS, FAIL, and NOT FOUND. The application also generates an Overall Security Summary based on the analysis results.

## Features

- Email authentication result analysis.
- PASS, FAIL, and NOT FOUND status identification.
- Overall Security Summary generation.
- Simple and user-friendly web interface.
- Dynamic result display.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java | Backend development |
| Jakarta Servlets | Request processing |
| HTML | Webpage structure |
| CSS | User interface styling |
| JavaScript | Frontend interactions |
| Apache Maven | Project build management |
| Apache Tomcat 10.1 | Application server |

## Project Structure

```text
SecureEmailAuthenticationGateway/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/emailgateway/servlet/
│       │       └── HeaderAnalyzerServlet.java
│       │
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml
│           ├── css/
│           │   └── style.css
│           ├── js/
│           │   └── script.js
│           └── index.html
│
├── pom.xml
├── .gitignore
└── README.md
```

## Requirements

- JDK 25
- Apache Maven
- Apache Tomcat 10.1.x

## How to Run the Project

1. Clone the repository:

   ```bash
   git clone https://github.com/divyanka22/SecureEmailAuthenticationGateway.git
   ```

2. Navigate to the project directory:

   ```bash
   cd SecureEmailAuthenticationGateway
   ```

3. Build the project:

   ```bash
   mvn clean package
   ```

4. Deploy the generated WAR file from the `target` directory to Apache Tomcat's `webapps` folder.

5. Start the Tomcat server.

6. Open the application in your browser:

   ```text
   http://localhost:8080/email-gateway/
   ```

## Testing

The application has been tested with the following authentication outcomes:

- PASS
- NOT FOUND
- FAIL

The Overall Security Summary feature was also tested to verify the generated assessment.

## GitHub Repository

https://github.com/divyanka22/SecureEmailAuthenticationGateway

## Author

Developed as an academic project.