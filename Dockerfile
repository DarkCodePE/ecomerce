FROM openjdk:8-jdk-slim
VOLUME "/tmp"
COPY "./target/ecomerce-0.0.1-SNAPSHOT.jar" "app.jar"
EXPOSE 8000
ENTRYPOINT ["java","-jar", "app.jar"]