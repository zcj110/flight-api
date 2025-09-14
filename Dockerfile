# # 第一阶段：构建应用
# FROM maven:3.8.6-jdk-21 AS build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests

# 第二阶段：只保留运行时所需内容
FROM eclipse-temurin:21-jre
# FROM openjdk:21-jre-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 