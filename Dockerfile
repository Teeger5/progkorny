FROM openjdk:17.0-jdk-slim-bullseye
COPY "./target/minecraft-servers-1.0-SNAPSHOT.jar" "/application/app.jar"
EXPOSE 8080
CMD ["java", "-jar", "/application/app.jar"]
