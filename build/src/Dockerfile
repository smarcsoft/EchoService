#FROM adoptopenjdk:11-jre-hotspot as build
#RUN echo 'Building echo service'
#RUN echo 'Installing maven'
FROM maven as build
WORKDIR /build
COPY src src
COPY config config
COPY pom.xml .
RUN mvn package
FROM adoptopenjdk:11-jre-hotspot as deploy
WORKDIR /app
COPY --from=build /build/target/grpcserver-1.1.0-jar-with-dependencies.jar /app
COPY --from=build /build/config/logging.properties /app 
ENTRYPOINT [ "java", "-Djava.util.logging.config.file=/app/logging.properties", "-jar", "/app/grpcserver-1.1.0-jar-with-dependencies.jar"]
VOLUME /home/docker_volume/logs
EXPOSE 50051
