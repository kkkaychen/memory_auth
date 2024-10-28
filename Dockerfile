# 使用 OpenJDK 作為基礎映像
FROM openjdk:17-jdk-slim

# 設置工作目錄
WORKDIR /app

# 複製編譯好的 JAR 文件到容器中
COPY target/memory_auth_microservice-0.0.1-SNAPSHOT.jar /app/security-service.jar

# 暴露應用端口（假設微服務運行在 8081 端口）
EXPOSE 8081

# 容器啟動時運行應用
ENTRYPOINT ["java", "-jar", "/app/security-service.jar"]
