FROM openjdk:11-jdk

ADD target/issuetracker-0.0.1-SNAPSHOT.jar issuetracker-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "issuetracker-0.0.1-SNAPSHOT.jar"]