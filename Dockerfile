#Stage 1: Build the application using Maven
FROM maven:3.9.4-eclipse-temurin-17 AS build

#Set the working directory in the build container
WORKDIR /app

#Copy the Maven project files into the container
COPY pom.xml .
COPY src ./src

#Build the application
RUN mvn clean package -DskipTests

#Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim as runtime

#Set the working directory in the runtime container
WORKDIR /app

#Copy the built JAR file from the build stage
COPY --from=build /app/target/pcbuilder-0.0.1-SNAPSHOT.jar app.jar

#Expose the port the app runs on
EXPOSE 8080

#Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]