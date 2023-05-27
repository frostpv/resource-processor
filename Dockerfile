FROM openjdk:11
EXPOSE 8081
ADD target/songs-processor-0.0.1.jar songs-processor-0.0.1.jar
ENTRYPOINT ["java", "-jar", "songs-processor-0.0.1.jar", "songs-processor-0.0.1.jar"]