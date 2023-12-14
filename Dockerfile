# Use an official Maven image as a build environment
FROM maven:3.8.3-openjdk-11 AS build

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the POM file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the application source code
COPY src ./src

# Build the application
RUN mvn clean package

# Use a smaller base image for the runtime environment
 
# Set the working directory in the container
WORKDIR /usr/src/app


RUN Runner.java


CMD [ "java","Runner" ]

# # Copy the JAR file from the build environment to the runtime environment
# COPY --from=build /usr/src/app/target/*.jar ./spark-java-app.jar

# # Specify the command to run on container start
# CMD ["java", "-jar", "spark-java-app.jar"]

