FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY auth-service/pom.xml auth-service/pom.xml
COPY chat-service/pom.xml chat-service/pom.xml
COPY collectible-service/pom.xml collectible-service/pom.xml
COPY community-service/pom.xml community-service/pom.xml
COPY discovery-service/pom.xml discovery-service/pom.xml
COPY forum-service/pom.xml forum-service/pom.xml
COPY game-service/pom.xml game-service/pom.xml
COPY gateway-service/pom.xml gateway-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml
COPY profile-service/pom.xml profile-service/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY collectible-service/src collectible-service/src

RUN mvn -pl collectible-service -am clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/collectible-service/target/collectible-service-*.jar app.jar

EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]
