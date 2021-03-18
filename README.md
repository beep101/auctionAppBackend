Requirements to run the app are

JDK11+ and Maven 3.6.3 or Spring Tool Suite 4.9.0

PostgreSQL database server with a role configured for Spring boot app

Node 14.15.5

NPM 7.5.4

To run the app proper configuration file is needed in directory /src/main/resources. File name application-dev.properties.example is mock for the config file. All keys in that file should be paired with proper values and .example extension removed.

There are two steps required to run the app from the command line. The first one is mvn install to install the project's dependencies and initialize the database. The second one is mvn spring-boot:run to run the application.

Their counterparts in STS IDE are Run->Run As->Maven Install and Run->Run As->Spring Boot App.

Before building frontend Node.js dependencies have to be installed via npm install command.

To build the frontend there are two commands, npm run-script watch and npm run-script build-prod. 

Npm run-script watch will run frontend in development mode and rebuild it when detects a change in frontend files. It should start before mvn spring-boot:run. 

Npm run-script build-prod will run only once and builds frontend in production mode.

Live at
auction-purple.herokuapp.com/
