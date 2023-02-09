FROM openjdk:8

ADD target/issuetracker-0.0.1-SNAPSHOT.jar issuetracker-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "issuetracker-0.0.1-SNAPSHOT.jar"]