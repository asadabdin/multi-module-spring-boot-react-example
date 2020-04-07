FROM maven:3.6.0-jdk-8-alpine as build

VOLUME /tmp
COPY target/scheduler-app-service*.jar scheduler-app-service.jar
# Run the jar file
EXPOSE 8081 8081
ENTRYPOINT ["java","-jar","/scheduler-app-service.jar"]
