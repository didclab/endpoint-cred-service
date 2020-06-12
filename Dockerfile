FROM openjdk:8-jdk-alpine
RUN apk update && apk add bash
RUN mkdir /app
WORKDIR /app
EXPOSE 8080
COPY target/endpoint-credentials-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","/app/endpoint-credentials-0.0.1-SNAPSHOT.jar"]