FROM eclipse-temurin-17-jre-alpine as builder
LABEL authors="CrusterCrew"
WORKDIR /app
COPY mvn pom.xml ./
COPY .mvn .mvn
run chmod +x mvnw && ./mvnw dependency:go-offline -q
copy src ./src
run ./mvnw package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
RUN addgroup -S crustercrewgroup && adduser -S crustercrew -G crustercrewgroup
user crustercrew
copy --from=builder /app/target/* /app/target/procurement-0.0.1-SNAPSHOT.jar
expose 8080
ENTRYPOINT ["java", "-jar", "/app/target/procurement-0.0.1-SNAPSHOT.jar"]