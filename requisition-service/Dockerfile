# Stage 1: Build JAR using JDK
FROM eclipse-temurin:17-jdk-alpine AS builder
LABEL authors="CrusterCrew"
WORKDIR /app
# Copy Maven wrapper & pom
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q
# Copy source code & compile
COPY src ./src
RUN ./mvnw clean package -DskipTests -q
# Stage 2: Runtime Image using JRE
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
RUN addgroup -S crustercrewgroup && adduser -S crustercrew -G crustercrewgroup
USER crustercrew
# Copy file .jar on stage builder
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]