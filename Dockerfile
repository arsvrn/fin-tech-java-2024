FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle gradlew gradlew.bat gradle /app/

COPY . .

RUN ./gradlew dependencies

RUN ./gradlew clean build

COPY build/libs/lessons-1.0-SNAPSHOT.jar /app/lessons.jar

ENTRYPOINT ["java", "-jar", "/app/lessons.jar"]

EXPOSE 8080