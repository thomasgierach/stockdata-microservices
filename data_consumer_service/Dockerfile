FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

RUN mvn -pl common,data_consumer_service -am clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/data_consumer_service/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]