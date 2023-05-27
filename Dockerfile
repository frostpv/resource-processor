FROM openjdk:11
EXPOSE 8081
ADD target/songs-processor-0.0.1-SNAPSHOT.jar songs-processor-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "songs-processor-0.0.1-SNAPSHOT.jar", "songs-processor-0.0.1-SNAPSHOT.jar"]