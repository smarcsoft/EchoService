#FROM adoptopenjdk:11-jre-hotspot as build
#RUN echo 'Building echo service'
#RUN echo 'Installing maven'
FROM maven as build
WORKDIR /build
COPY src src
COPY pom.xml .
RUN mvn package
FROM adoptopenjdk:11-jre-hotspot as deploy
WORKDIR /app
COPY --from=build /build/target/grpcserver-1.0.0-jar-with-dependencies.jar /app
ENTRYPOINT [ "java", "-jar", "/app/grpcserver-1.0.0-jar-with-dependencies.jar"]
EXPOSE 50051