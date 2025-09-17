FROM ubuntu:24.04
LABEL authors="alexandersilvennoinen"

RUN apt-get update && apt-get install openjdk-21-jdk -y

WORKDIR /app

COPY . .

RUN ./gradlew assemble

FROM ubuntu:24.04

RUN apt-get update && apt-get install openjdk-21-jre -y

COPY --from=0 /app/build/libs/s9_java_exchange_adapter-0.0.1-SNAPSHOT.jar /app/build/libs/s9_java_exchange_adapter-0.0.1-SNAPSHOT.jar

EXPOSE 8080:8080
CMD ["/usr/bin/java", "-jar", "/app/build/libs/s9_java_exchange_adapter-0.0.1-SNAPSHOT.jar"]